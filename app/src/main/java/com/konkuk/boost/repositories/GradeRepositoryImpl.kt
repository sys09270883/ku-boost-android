package com.konkuk.boost.repositories

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.konkuk.boost.api.AuthorizedKuisService
import com.konkuk.boost.api.OzService
import com.konkuk.boost.data.grade.*
import com.konkuk.boost.persistence.PreferenceManager
import com.konkuk.boost.persistence.area.SubjectAreaDao
import com.konkuk.boost.persistence.area.SubjectAreaEntity
import com.konkuk.boost.persistence.grade.GradeContract
import com.konkuk.boost.persistence.grade.GradeDao
import com.konkuk.boost.persistence.grade.GradeEntity
import com.konkuk.boost.persistence.rank.RankDao
import com.konkuk.boost.persistence.rank.RankEntity
import com.konkuk.boost.persistence.simul.GraduationSimulationDao
import com.konkuk.boost.persistence.simul.GraduationSimulationEntity
import com.konkuk.boost.utils.DateTimeConverter
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.OzEngine
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.component.KoinApiExtension

class GradeRepositoryImpl(
    private val authorizedKuisService: AuthorizedKuisService,
    private val graduationSimulationDao: GraduationSimulationDao,
    private val preferenceManager: PreferenceManager,
    private val gradeDao: GradeDao,
    private val rankDao: RankDao,
    private val ozService: OzService,
    private val subjectAreaDao: SubjectAreaDao,
) : GradeRepository {
    override suspend fun makeGraduationSimulationRequest(): UseCase<GraduationSimulationResponse> {
        val username = preferenceManager.username
        val stdNo = preferenceManager.stdNo
        val courseYear = stdNo.toString().substring(0, 4).toInt()
        val code = preferenceManager.code

        val graduationSimulationResponse: GraduationSimulationResponse

        try {
            if (username.isEmpty()) throw Exception("사용자 이름이 없습니다.")

            graduationSimulationResponse = authorizedKuisService.fetchGraduationSimulation(
                stdNo = stdNo,
                year = courseYear,
                shregCd = code
            )

            val simulations = graduationSimulationResponse.simulations

            for (simulation in simulations) {
                val data = GraduationSimulationEntity(
                    username = username,
                    classification = simulation.classification,
                    standard = simulation.standard ?: 0,
                    acquired = simulation.acquired?.toInt() ?: 0,   // String으로 넘어 옴.
                    remainder = simulation.remainder ?: 0,
                    modifiedAt = System.currentTimeMillis()
                )
                graduationSimulationDao.insertGraduationSimulation(data)
            }

        } catch (e: Exception) {
            Log.e("ku-boost", "${e.message}")
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(graduationSimulationResponse)
    }

    override suspend fun makeUserInformationRequest(): UseCase<UserInformationResponse> {
        val userInfoResponse: UserInformationResponse
        try {
            userInfoResponse = authorizedKuisService.fetchUserInformation()
            withContext(Dispatchers.IO) {
                userInfoResponse.userInformation.apply {
                    preferenceManager.setUserInfo(
                        name = name ?: "",
                        stdNo = stdNo.toInt(),  // API stdNo는 String
                        state = state ?: "",
                        dept = dept ?: "",
                        code = code
                    )
                }
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(userInfoResponse)
    }

    override suspend fun getGraduationSimulations(): UseCase<List<GraduationSimulationEntity>> {
        val graduationSimulationList: List<GraduationSimulationEntity>

        try {
            withContext(Dispatchers.IO) {
                val username = preferenceManager.username
                graduationSimulationList =
                    graduationSimulationDao.loadGraduationSimulationByUsername(username)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(graduationSimulationList)
    }

    override fun getStdNo(): Int = preferenceManager.stdNo

    private suspend fun fetchValidGrades(): List<ValidGrade> {
        val stdNo = preferenceManager.stdNo
        val validGradesResponse: ValidGradesResponse
        val validGrades: List<ValidGrade>

        try {
            validGradesResponse = authorizedKuisService.fetchValidGrades(stdNo = stdNo)
            validGrades = validGradesResponse.validGrades
        } catch (e: Exception) {
            return emptyList()
        }

        return validGrades
    }

    private suspend fun fetchAllGrades(): List<GradeEntity> {
        val allGrades = mutableListOf<GradeEntity>()
        // 1학기: 1, 여름학기: 2, 2학기: 3, 겨울학기: 4
        val semesterConverter = hashMapOf(1 to 1, 4 to 2, 2 to 3, 5 to 4)

        try {
            val stdNo = preferenceManager.stdNo
            val username = preferenceManager.username
            val startYear = stdNo.toString().substring(0, 4).toInt()
            val endYear = DateTimeConverter.currentYear().toInt()
            val semesters = intArrayOf(5, 2, 4, 1)

            var isLastSemesterQueried = false

            for (year in startYear..endYear) {
                for (semester in semesters) {
                    val gradeResponse = authorizedKuisService.fetchRegularGrade(
                        stdNo,
                        year,
                        "B0101$semester",
                        DateTimeConverter.today()
                    )

                    if (year == endYear && !isLastSemesterQueried && gradeResponse.grades.isNotEmpty()) {
                        gradeDao.removeGrades(username, year, semesterConverter[semester]!!)
                        isLastSemesterQueried = true
                    }

                    for (grade in gradeResponse.grades) {
                        allGrades += GradeEntity(
                            username,
                            grade.evaluationMethod ?: "미정",
                            year,
                            semesterConverter[semester]!!,
                            grade.classification,
                            grade.characterGrade ?: "",
                            grade.grade ?: 0.0f,
                            grade.professor ?: "",
                            grade.subjectId,
                            grade.subjectName ?: "",
                            grade.subjectNumber ?: "",
                            grade.subjectPoint ?: 0,
                            "",
                            GradeContract.Type.PENDING.value,
                        )
                    }
                }
            }

        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return emptyList()
        }

        return allGrades
    }

    @KoinApiExtension
    override suspend fun makeValidGradesAndUpdateClassification(): UseCase<Unit> {
        val stdNo = preferenceManager.stdNo
        val username = preferenceManager.username
        val grades: MutableList<GradeEntity>

        try {
            withContext(Dispatchers.IO) {
                // fetch all grades and do grades validation.
                val deferredAllGrades = async {
                    fetchAllGrades()
                }
                val deferredValidGrades = async {
                    fetchValidGrades()
                }

                val allGrades = deferredAllGrades.await()
                val validGrades = deferredValidGrades.await()

                for (validGrade in validGrades) {
                    val grade = allGrades.find { grade -> grade.subjectId == validGrade.subjectId }
                        ?: continue
                    grade.type = GradeContract.Type.VALID.value
                }

                grades = allGrades.toMutableList()

                // Update classification of graduation simulation.
                val oz = OzEngine.getInstance(username, stdNo.toString())
                val file = oz.makeSimulFile()

                val params = file.readBytes()
                val requestBody = params.toRequestBody(
                    "application/octet-stream".toMediaTypeOrNull(),
                    0,
                    params.size
                )

                val responseBody = ozService.postOzBinary(requestBody)
                val stream = responseBody.byteStream()

                val (simulMap, electiveMap) = oz.getSimulMap(stream)
                val subjectAreaList = mutableListOf<SubjectAreaEntity>()

                for (item in electiveMap) {
                    for (elective in item.value) {
                        val type = when (item.key) {
                            "basic" -> 1
                            "core" -> 2
                            else -> 0
                        }

                        if (type > 0) {
                            subjectAreaList.add(SubjectAreaEntity(username, type, elective))
                        }
                    }
                }

                subjectAreaDao.insert(*subjectAreaList.toTypedArray())

                for ((clf, subjectIdList) in simulMap) {
                    for (subjectId in subjectIdList) {
                        val onlySubjectNumber = subjectId.substring(0, 9)
                        var subjectArea = subjectId.substring(9)

                        subjectArea = when (clf) {
                            "기교" -> GradeUtils.basic(subjectArea)
                            "심교", "핵교" -> GradeUtils.core(subjectArea)
                            else -> ""
                        }

                        val grade =
                            grades.find { grade -> grade.subjectNumber == onlySubjectNumber }
                                ?: continue

                        grade.subjectArea = subjectArea
                        grade.classification = clf
                    }
                }

                gradeDao.insertGrade(*grades.toTypedArray())
                preferenceManager.hasData = true
            }
        } catch (e: Exception) {
            Log.e("ku-boost", "${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(Unit)
    }

    override suspend fun getAllValidGrades(): UseCase<List<GradeEntity>> {
        val username = preferenceManager.username

        val allValidGrades: List<GradeEntity>
        try {
            allValidGrades = gradeDao.getAllValidGrades(username)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(allValidGrades)
    }

    override suspend fun getNotDeletedGrades(): UseCase<List<GradeEntity>> {
        val username = preferenceManager.username

        val allGrades: List<GradeEntity>
        try {
            allGrades = gradeDao.getNotDeletedGrades(username)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(allGrades)
    }

    override suspend fun getCurrentGrades(): UseCase<List<GradeEntity>> {
        val username = preferenceManager.username

        val currentGrades: List<GradeEntity>
        try {
            currentGrades = gradeDao.getCurrentSemesterGradesTransaction(username)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(currentGrades)
    }

    override fun hasData() = preferenceManager.hasData

    override suspend fun getNotDeletedGradesByClassification(clf: String): UseCase<List<GradeEntity>> {
        val username = preferenceManager.username

        val gradesByClassification: List<GradeEntity>
        try {
            gradesByClassification = gradeDao.getNotDeletedGradesByClassification(username, clf)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(gradesByClassification)
    }

    override suspend fun getTotalRank(year: Int, semester: Int): UseCase<RankEntity> {
        val username = preferenceManager.username

        val totalRank: RankEntity
        try {
            totalRank = rankDao.get(username, year, semester)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(totalRank)
    }

    @KoinApiExtension
    override suspend fun makeTotalRankAndUpdateDeletedSubjects(): UseCase<Unit> {
        val username = preferenceManager.username
        val stdNo = preferenceManager.stdNo

        try {
            val oz = OzEngine.getInstance(username, stdNo.toString())
            val file = oz.makeGradeFile()

            val params = file.readBytes()
            val requestBody = params.toRequestBody(
                "application/octet-stream".toMediaTypeOrNull(),
                0,
                params.size
            )

            val responseBody = ozService.postOzBinary(requestBody)
            val (rankMap, deletedSubjects) = oz.getRankMapAndDeletedSubjects(responseBody.byteStream())

            val ranks = mutableListOf<RankEntity>()
            for ((key, value) in rankMap) {
                ranks += RankEntity(username, key.year, key.semester, value.rank, value.total)
            }

            for (deletedSubject in deletedSubjects) {
                gradeDao.updateType(username, deletedSubject, GradeContract.Type.DELETED.value)
            }

            rankDao.insert(*ranks.toTypedArray())
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(Unit)
    }

    override suspend fun getAllSubjectArea(): UseCase<List<SubjectAreaEntity>> {
        val username = preferenceManager.username
        val subjectAreas: List<SubjectAreaEntity>

        try {
            subjectAreas = subjectAreaDao.getAll(username)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(subjectAreas)
    }

    override suspend fun getSubjectAreaCounts(): UseCase<List<SubjectAreaCount>> {
        val username = preferenceManager.username
        val stdNo = preferenceManager.stdNo
        val year = stdNo / 100_000
        val basicType = "기교"
        val coreType = if (year > 2015) "심교" else "핵교"
        val subjectAreas: List<SubjectAreaEntity>
        val subjectAreaCounts = mutableListOf<SubjectAreaCount>()
        val basicGrades: List<GradeEntity>
        val coreGrades: List<GradeEntity>

        try {
            subjectAreas = subjectAreaDao.getAll(username)
            for (area in subjectAreas) {
                subjectAreaCounts += SubjectAreaCount(area)
            }
            basicGrades = gradeDao.getNotDeletedGradesByClassification(username, basicType)
            coreGrades = gradeDao.getNotDeletedGradesByClassification(username, coreType)

            for (grade in basicGrades) {
                for (areaWithCount in subjectAreaCounts) {
                    val area = areaWithCount.area

                    if (area.type != 1) {
                        continue
                    }

                    if (area.subjectAreaName == grade.subjectArea && grade.type == GradeContract.Type.VALID.value) {
                        areaWithCount.count += 1
                    }
                }
            }

            for (grade in coreGrades) {
                for (areaWithCount in subjectAreaCounts) {
                    val area = areaWithCount.area

                    if (area.type != 2) {
                        continue
                    }

                    if (area.subjectAreaName == grade.subjectArea && grade.type == GradeContract.Type.VALID.value) {
                        areaWithCount.count += 1
                    }
                }
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(subjectAreaCounts)
    }

}