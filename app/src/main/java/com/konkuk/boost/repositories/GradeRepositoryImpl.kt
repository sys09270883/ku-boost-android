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
import com.konkuk.boost.utils.*
import kotlinx.coroutines.Deferred
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

            withContext(Dispatchers.IO) {
                graduationSimulationResponse = authorizedKuisService.fetchGraduationSimulation(
                    stdNo = stdNo,
                    year = courseYear,
                    shregCd = code
                )

                val simulations = graduationSimulationResponse.simulations ?: emptyList()

                val simulationEntities = mutableListOf<GraduationSimulationEntity>()
                for (simulation in simulations) {
                    simulationEntities += GraduationSimulationEntity(
                        username = username,
                        classification = simulation.classification,
                        standard = simulation.standard ?: 0,
                        acquired = simulation.acquired?.toInt() ?: 0,   // String으로 넘어 옴.
                        remainder = simulation.remainder ?: 0,
                        modifiedAt = System.currentTimeMillis()
                    )
                }

                graduationSimulationDao.insertGraduationSimulation(*simulationEntities.toTypedArray())
            }
        } catch (e: Exception) {
            Log.e(MessageUtils.LOG_KEY, "${e.message}")
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(graduationSimulationResponse)
    }

    override suspend fun makeUserInformationRequest(): UseCase<UserInformationResponse> {
        val userInfoResponse: UserInformationResponse
        try {
            withContext(Dispatchers.IO) {
                Log.d(MessageUtils.LOG_KEY, "Kuis cookie: ${preferenceManager.loginCookie}")
                userInfoResponse = authorizedKuisService.fetchUserInformation()
                val userInfo = userInfoResponse.userInformation
                Log.d(MessageUtils.LOG_KEY, "User info: $userInfo")

                preferenceManager.setUserInfo(
                    userInfo.name ?: "",
                    userInfo.stdNo.toInt(),
                    userInfo.state ?: "",
                    userInfo.dept ?: "",
                    userInfo.code
                )
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("UserInfoResponse error: ${e.message}")
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

    private suspend fun fetchValidGrades(stdNo: Int): List<ValidGrade> {
        val validGradesResponse: ValidGradesResponse?
        val validGrades: List<ValidGrade>

        try {
            validGradesResponse = authorizedKuisService.fetchValidGrades(stdNo)
            validGrades = validGradesResponse?.validGrades ?: emptyList()
        } catch (e: Exception) {
            Log.e(MessageUtils.LOG_KEY, "${e.message}")
            return emptyList()
        }

        return validGrades
    }

    private suspend fun fetchAllGrades(username: String, stdNo: Int): List<GradeEntity> {
        val allGrades = mutableListOf<GradeEntity>()
        // 1학기: 1, 여름학기: 2, 2학기: 3, 겨울학기: 4
        val semesterConverter = hashMapOf(1 to 1, 4 to 2, 2 to 3, 5 to 4)

        try {
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

    private suspend fun getUpdatedGrades(username: String, stdNo: Int) =
        withContext(Dispatchers.IO) {
            val deferredAllGrades = async {
                fetchAllGrades(username, stdNo)
            }
            val deferredValidGrades = async {
                fetchValidGrades(stdNo)
            }

            val allGrades = deferredAllGrades.await()
            val validGrades = deferredValidGrades.await()

            for (validGrade in validGrades) {
                val grade = allGrades.find { grade ->
                    grade.subjectId == validGrade.subjectId
                            && grade.year == validGrade.year.toInt()
                            && GradeUtils.convertToSemesterCode(grade.semester) == validGrade.semesterCode
                } ?: continue
                grade.type = GradeContract.Type.VALID.value
            }

            allGrades.toMutableList()
        }

    private fun updateSubjectAreaAndClassification(
        simulMap: Map<String, List<String>>,
        grades: List<GradeEntity>
    ) {
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
    }

    private fun makeSubjectAreaList(
        username: String,
        electiveMap: Map<String, Set<String>>
    ): List<SubjectAreaEntity> {
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

        return subjectAreaList
    }

    @KoinApiExtension
    private suspend fun getSubjectAreaList(
        username: String,
        simulMap: Map<String, List<String>>,
        electiveMap: Map<String, Set<String>>,
        grades: List<GradeEntity>
    ) = withContext(Dispatchers.IO) {
        updateSubjectAreaAndClassification(simulMap, grades)
        makeSubjectAreaList(username, electiveMap)
    }

    @KoinApiExtension
    private suspend fun makeSimulStream(oz: OzEngine) = withContext(Dispatchers.IO) {
        val file = oz.makeSimulFile()

        val params = file.readBytes()
        val requestBody = params.toRequestBody(
            "application/octet-stream".toMediaTypeOrNull(),
            0,
            params.size
        )

        val responseBody = ozService.postOzBinary(requestBody)
        val stream = responseBody.byteStream()
        stream
    }

    @KoinApiExtension
    private suspend fun makeGradeStream(oz: OzEngine) = withContext(Dispatchers.IO) {
        val file = oz.makeGradeFile()

        val params = file.readBytes()
        val requestBody = params.toRequestBody(
            "application/octet-stream".toMediaTypeOrNull(),
            0,
            params.size
        )

        val responseBody = ozService.postOzBinary(requestBody)
        val stream = responseBody.byteStream()
        stream
    }

    @KoinApiExtension
    override suspend fun makeValidGradesAndUpdateClassification(): UseCase<Unit> {
        val stdNo = preferenceManager.stdNo
        val username = preferenceManager.username

        try {
            withContext(Dispatchers.IO) {
                val deferredGrades = async {
                    getUpdatedGrades(username, stdNo)
                }

                val oz = OzEngine.getInstance(username, stdNo)
                val simulStream = makeSimulStream(oz)
                oz.makeSimulContent(simulStream)

                val deferredSimulMap = async {
                    oz.getSimulMap()
                }
                val deferredElectiveMap = async {
                    oz.getElectiveMap()
                }

                val grades = deferredGrades.await()
                val simulMap = deferredSimulMap.await()
                val electiveMap = deferredElectiveMap.await()

                val subjectAreaList = getSubjectAreaList(
                    username,
                    simulMap,
                    electiveMap,
                    grades
                )

                subjectAreaDao.insert(*subjectAreaList.toTypedArray())
                gradeDao.insertGrade(*grades.toTypedArray())
                preferenceManager.hasData = true
            }
        } catch (e: Exception) {
            Log.e(MessageUtils.LOG_KEY, "${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(Unit)
    }

    override suspend fun getAllValidGrades(): UseCase<List<GradeEntity>> {
        val username = preferenceManager.username

        val allValidGrades: List<GradeEntity>
        try {
            withContext(Dispatchers.IO) {
                allValidGrades = gradeDao.getAllValidGrades(username)
            }
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
            withContext(Dispatchers.IO) {
                allGrades = gradeDao.getNotDeletedGrades(username)
            }
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
            withContext(Dispatchers.IO) {
                currentGrades = gradeDao.getCurrentSemesterGradesTransaction(username)
            }
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
            withContext(Dispatchers.IO) {
                gradesByClassification = gradeDao.getNotDeletedGradesByClassification(username, clf)
            }
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
            withContext(Dispatchers.IO) {
                totalRank = rankDao.get(username, year, semester)
            }
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
            withContext(Dispatchers.IO) {
                val oz = OzEngine.getInstance(username, stdNo.toString())
                val gradeStream = makeGradeStream(oz)
                oz.makeGradeContent(gradeStream)

                val rankMap = oz.getRankMap()
                val deletedSubjects = oz.getDeletedSubjects()

                val ranks = mutableListOf<RankEntity>()
                for ((key, value) in rankMap) {
                    ranks += RankEntity(username, key.year, key.semester, value.rank, value.total)
                }

                for (deletedSubject in deletedSubjects) {
                    gradeDao.updateType(
                        username,
                        deletedSubject.first,
                        GradeContract.Type.DELETED.value,
                        deletedSubject.second,
                        deletedSubject.third
                    )
                }

                rankDao.insert(*ranks.toTypedArray())
            }
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
        val deferredSubjectAreas: Deferred<List<SubjectAreaEntity>>
        val subjectAreaCounts = mutableListOf<SubjectAreaCount>()
        val deferredBasicGrades: Deferred<List<GradeEntity>>
        val deferredCoreGrades: Deferred<List<GradeEntity>>

        try {
            withContext(Dispatchers.IO) {
                deferredSubjectAreas = async {
                    subjectAreaDao.getAll(username)
                }
                deferredBasicGrades = async {
                    gradeDao.getNotDeletedGradesByClassification(username, basicType)
                }
                deferredCoreGrades = async {
                    gradeDao.getNotDeletedGradesByClassification(username, coreType)
                }

                val subjectAreas = deferredSubjectAreas.await()
                val basicGrades = deferredBasicGrades.await()
                val coreGrades = deferredCoreGrades.await()

                for (area in subjectAreas) {
                    subjectAreaCounts += SubjectAreaCount(area)
                }

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
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(subjectAreaCounts)
    }

}