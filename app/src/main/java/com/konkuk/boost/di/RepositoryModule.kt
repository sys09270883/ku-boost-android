package com.konkuk.boost.di

import android.util.Log
import com.konkuk.boost.api.AuthService
import com.konkuk.boost.api.GradeService
import com.konkuk.boost.data.UseCase
import com.konkuk.boost.data.auth.AuthRepository
import com.konkuk.boost.data.auth.LoginResponse
import com.konkuk.boost.data.grade.GradeRepository
import com.konkuk.boost.data.grade.GraduationSimulationResponse
import com.konkuk.boost.data.grade.UserInformationResponse
import com.konkuk.boost.data.grade.ValidGradesResponse
import com.konkuk.boost.persistence.*
import com.konkuk.boost.utils.DateTimeConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.dsl.module
import retrofit2.Response

val repositoryModule = module {
    fun provideLoginRepository(
        authService: AuthService,
        settingsManager: SettingsManager
    ): AuthRepository {
        return object : AuthRepository {
            override suspend fun makeLoginRequest(
                username: String,
                password: String
            ): UseCase<LoginResponse> {
                val loginResponse: Response<LoginResponse>
                try {
                    loginResponse = authService.login(username, password)
                } catch (exception: Exception) {
                    Log.e("yoonseop", "${exception.message}")
                    return UseCase.error("서버에 문제가 발생했습니다.")
                }

                val cookie = loginResponse.headers()["Set-Cookie"]?.split(";")?.first()
                    ?: return UseCase.error("다시 로그인하세요.")
                settingsManager.setCookie(cookie)

                val loginBody = loginResponse.body()
                val loginSuccess =
                    loginBody?.loginSuccess ?: return UseCase.error("아이디, 비밀번호를 확인하세요.")
                val loginFailure = loginBody.loginFailure

                return when {
                    loginSuccess.isSucceeded -> {
                        settingsManager.setAuthInfo(username, password)
                        UseCase.success(loginBody)
                    }
                    loginFailure != null -> UseCase.error(loginFailure.errorMessage)
                    else -> UseCase.error("서버에 문제가 발생했습니다.")
                }
            }

            override suspend fun makeAutoLoginRequest(): UseCase<LoginResponse> {
                val username = settingsManager.usernameFlow.first()
                val password = settingsManager.passwordFlow.first()

                if (username.isBlank() || password.isBlank()) {
                    settingsManager.setAuthInfo("", "")
                    return UseCase.error("아이디, 비밀번호를 확인하세요.")
                }

                val loginResponse: Response<LoginResponse>
                try {
                    loginResponse = authService.login(username, password)
                } catch (exception: Exception) {
                    Log.e("yoonseop", "${exception.message}")
                    return UseCase.error("네트워크를 확인하세요.")
                }

                val cookie = loginResponse.headers()["Set-Cookie"]?.split(";")?.first()
                    ?: return UseCase.error("다시 로그인하세요.")
                settingsManager.setCookie(cookie)

                val loginBody = loginResponse.body()
                val loginSuccess =
                    loginBody?.loginSuccess ?: return UseCase.error("아이디, 비밀번호를 확인하세요.")
                val loginFailure = loginBody.loginFailure

                return when {
                    loginSuccess.isSucceeded -> UseCase.success(loginBody)
                    loginFailure != null -> UseCase.error(loginFailure.errorMessage)
                    else -> UseCase.error("서버에 문제가 발생했습니다.")
                }
            }

            override suspend fun makeLogoutRequest(): UseCase<Unit> {
                settingsManager.setAuthInfo("", "")
                return UseCase.success(Unit)
            }
        }
    }

    fun provideGradeRepository(
        gradeService: GradeService,
        graduationSimulationDao: GraduationSimulationDao,
        settingsManager: SettingsManager,
        gradeDao: GradeDao
    ): GradeRepository {
        return object : GradeRepository {
            override suspend fun makeGraduationSimulationRequest(): UseCase<GraduationSimulationResponse> {
                val username = settingsManager.usernameFlow.first()
                val stdNo = settingsManager.stdNoFlow.first()
                val corsYy = stdNo.toString().substring(0, 4).toInt()
                val shregCd = settingsManager.codeFlow.first()

                val graduationSimulationResponse: GraduationSimulationResponse

                try {
                    if (username == "") throw Exception("Username is empty.")

                    graduationSimulationResponse = gradeService.fetchGraduationSimulation(
                        stdNo = stdNo,
                        year = corsYy,
                        shregCd = shregCd
                    )

                    val simulations = graduationSimulationResponse.simulations

                    for (simulation in simulations) {
                        val data = GraduationSimulationEntity(
                            username = username,
                            classification = simulation.classification,
                            standard = simulation.standard,
                            acquired = simulation.acquired.toInt(),     // API에서 String으로 넘어 옴.
                            remainder = simulation.remainder,
                            modifiedAt = System.currentTimeMillis()
                        )
                        graduationSimulationDao.insertGraduationSimulation(data)
                    }

                } catch (exception: Exception) {
                    return UseCase.error("${exception.message}")
                }

                return UseCase.success(graduationSimulationResponse)
            }

            override suspend fun makeUserInformationRequest(): UseCase<UserInformationResponse> {
                val userInfoResponse: UserInformationResponse
                try {
                    userInfoResponse = gradeService.fetchUserInformation()
                    userInfoResponse.userInformation.apply {
                        settingsManager.setUserInfo(
                            name = name,
                            stdNo = stdNo.toInt(),  // API stdNo는 String
                            state = state,
                            dept = dept,
                            code = code
                        )
                    }
                } catch (exception: Exception) {
                    Log.e("yoonseop", "${exception.message}")
                    return UseCase.error("${exception.message}")
                }

                return UseCase.success(userInfoResponse)
            }

            override suspend fun getGraduationSimulations(): UseCase<List<GraduationSimulationEntity>> {
                val username = settingsManager.usernameFlow.first()
                val graduationSimulationList: List<GraduationSimulationEntity>

                try {
                    graduationSimulationList =
                        graduationSimulationDao.loadGraduationSimulationByUsername(username).first()
                } catch (exception: Exception) {
                    return UseCase.error("${exception.message}")
                }

                return UseCase.success(graduationSimulationList)
            }

            override fun getStdNoFlow(): Flow<Int> = settingsManager.stdNoFlow

            override suspend fun makeAllGradesRequest(): UseCase<Unit> {
                val allGrades = mutableListOf<GradeEntity>()
                // 1학기: 1, 여름학기: 2, 2학기: 3, 겨울학기: 4
                val semesterConverter = hashMapOf(1 to 1, 4 to 2, 2 to 3, 5 to 4)

                try {
                    val stdNo = settingsManager.stdNoFlow.first()
                    val username = settingsManager.usernameFlow.first()
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
                                    grade = grade.grade,
                                    professor = grade.professor,
                                    subjectId = grade.subjectId,
                                    subjectName = grade.subjectName,
                                    subjectNumber = grade.subjectNumber,
                                    subjectPoint = grade.subjectPoint,
                                    valid = false,
                                    modifiedAt = System.currentTimeMillis()
                                )
                            }
                        }
                    }

                    gradeDao.insertGrade(*allGrades.toTypedArray())

                } catch (exception: Exception) {
                    Log.e("yoonseop", "${exception.message}")
                    return UseCase.error("${exception.message}")
                }

                return UseCase.success(Unit)
            }

            override suspend fun makeAllValidGradesRequest(): UseCase<Unit> {
                val stdNo = settingsManager.stdNoFlow.first()
                val username = settingsManager.usernameFlow.first()
                val validGradesResponse: ValidGradesResponse

                try {
                    validGradesResponse = gradeService.fetchValidGrades(stdNo = stdNo)
                    val validGrades = validGradesResponse.validGrades
                    for (validGrade in validGrades) {
                        gradeDao.updateValid(username, validGrade.subjectId, true)
                    }
                } catch (exception: Exception) {
                    return UseCase.error("${exception.message}")
                }
                return UseCase.success(Unit)
            }

            override suspend fun getAllValidGrades(): UseCase<List<GradeEntity>> {
                val username = settingsManager.usernameFlow.first()

                val allValidGrades: List<GradeEntity>
                try {
                    allValidGrades = gradeDao.getAllGrades(username)
                } catch (exception: Exception) {
                    return UseCase.error("${exception.message}")
                }

                return UseCase.success(allValidGrades)
            }

            override suspend fun getCurrentGrades(): UseCase<List<GradeEntity>> {
                val username = settingsManager.usernameFlow.first()

                val currentGrades: List<GradeEntity>
                try {
                    currentGrades = gradeDao.getCurrentSemesterGradesTransaction(username)
                } catch (exception: Exception) {
                    Log.e("yoonseop", "${exception.message}")
                    return UseCase.error("${exception.message}")
                }

                return UseCase.success(currentGrades)
            }

        }
    }

    single { provideLoginRepository(get(), get()) }
    single { provideGradeRepository(get(), get(), get(), get()) }
}