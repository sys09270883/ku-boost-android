package com.konkuk.boost.repositories

import android.content.res.AssetManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.konkuk.boost.api.GradeService
import com.konkuk.boost.api.OzService
import com.konkuk.boost.data.grade.GraduationSimulationResponse
import com.konkuk.boost.data.grade.UserInformationResponse
import com.konkuk.boost.data.grade.ValidGradesResponse
import com.konkuk.boost.persistence.*
import com.konkuk.boost.utils.DateTimeConverter
import com.konkuk.boost.utils.OzEngine
import com.konkuk.boost.utils.UseCase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.component.KoinApiExtension

class GradeRepositoryImpl(
    private val gradeService: GradeService,
    private val graduationSimulationDao: GraduationSimulationDao,
    private val preferenceManager: PreferenceManager,
    private val gradeDao: GradeDao,
    private val rankDao: RankDao,
    private val ozService: OzService
) : GradeRepository {
    override suspend fun makeGraduationSimulationRequest(): UseCase<GraduationSimulationResponse> {
        val username = preferenceManager.username
        val stdNo = preferenceManager.stdNo
        val courseYear = stdNo.toString().substring(0, 4).toInt()
        val code = preferenceManager.code

        val graduationSimulationResponse: GraduationSimulationResponse

        try {
            if (username.isEmpty()) throw Exception("사용자 이름이 없습니다.")

            graduationSimulationResponse = gradeService.fetchGraduationSimulation(
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
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(graduationSimulationResponse)
    }

    override suspend fun makeUserInformationRequest(): UseCase<UserInformationResponse> {
        val userInfoResponse: UserInformationResponse
        try {
            userInfoResponse = gradeService.fetchUserInformation()
            userInfoResponse.userInformation.apply {
                preferenceManager.setUserInfo(
                    name = name ?: "",
                    stdNo = stdNo.toInt(),  // API stdNo는 String
                    state = state ?: "",
                    dept = dept ?: "",
                    code = code
                )
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(userInfoResponse)
    }

    override suspend fun getGraduationSimulations(): UseCase<List<GraduationSimulationEntity>> {
        val username = preferenceManager.username
        val graduationSimulationList: List<GraduationSimulationEntity>

        try {
            graduationSimulationList =
                graduationSimulationDao.loadGraduationSimulationByUsername(username)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(graduationSimulationList)
    }

    override fun getStdNo(): Int = preferenceManager.stdNo

    override suspend fun makeAllGradesRequest(): UseCase<Unit> {
        val allGrades = mutableListOf<GradeEntity>()
        // 1학기: 1, 여름학기: 2, 2학기: 3, 겨울학기: 4
        val semesterConverter = hashMapOf(1 to 1, 4 to 2, 2 to 3, 5 to 4)

        try {
            val stdNo = preferenceManager.stdNo
            val username = preferenceManager.username
            val startYear = stdNo.toString().substring(0, 4).toInt()
            val endYear = DateTimeConverter.currentYear().toInt()
            val semesters = intArrayOf(1, 4, 2, 5)

            for (year in startYear..endYear) {
                for (semester in semesters) {
                    val gradeResponse = gradeService.fetchRegularGrade(
                        stdNo = stdNo,
                        year = year,
                        semester = "B0101$semester",
                        curDate = DateTimeConverter.today()
                    )

                    for (grade in gradeResponse.grades) {
                        allGrades += GradeEntity(
                            username = username,
                            evaluationMethod = grade.evaluationMethod ?: "미정",
                            year = year,
                            semester = semesterConverter[semester]!!,
                            classification = grade.classification,
                            characterGrade = grade.characterGrade ?: "",
                            grade = grade.grade ?: 0.0f,
                            professor = grade.professor ?: "",
                            subjectId = grade.subjectId,
                            subjectName = grade.subjectName ?: "",
                            subjectNumber = grade.subjectNumber ?: "",
                            subjectPoint = grade.subjectPoint ?: 0,
                            valid = false,
                            modifiedAt = System.currentTimeMillis()
                        )
                    }
                }
            }

            gradeDao.insertGrade(*allGrades.toTypedArray())
            preferenceManager.hasData = true
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(Unit)
    }

    override suspend fun makeAllValidGradesRequest(): UseCase<Unit> {
        val stdNo = preferenceManager.stdNo
        val username = preferenceManager.username
        val validGradesResponse: ValidGradesResponse

        try {
            validGradesResponse = gradeService.fetchValidGrades(stdNo = stdNo)
            val validGrades = validGradesResponse.validGrades
            for (validGrade in validGrades) {
                gradeDao.updateValid(username, validGrade.subjectId, true)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }
        return UseCase.success(Unit)
    }

    override suspend fun getAllValidGrades(): UseCase<List<GradeEntity>> {
        val username = preferenceManager.username

        val allValidGrades: List<GradeEntity>
        try {
            allValidGrades = gradeDao.getAllGrades(username)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(allValidGrades)
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

    override suspend fun getGradesByClassification(clf: String): UseCase<List<GradeEntity>> {
        val username = preferenceManager.username

        val gradesByClassification: List<GradeEntity>
        try {
            gradesByClassification = gradeDao.getGradesByClassification(username, clf)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(gradesByClassification)
    }

    override suspend fun getTotalRank(): UseCase<RankEntity> {
        val username = preferenceManager.username

        val totalRank: RankEntity
        try {
            totalRank = rankDao.getTotalRank(username)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(totalRank)
    }

    @KoinApiExtension
    override suspend fun makeTotalRank(am: AssetManager): UseCase<Unit> {
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
            val rankMap = oz.getRankMap(responseBody.byteStream())

            val ranks = mutableListOf<RankEntity>()
            for ((key, value) in rankMap) {
                ranks += RankEntity(username, key.year, key.semester, value.rank, value.total)
            }

            rankDao.insert(*ranks.toTypedArray())
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(Unit)
    }

}