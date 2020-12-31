package com.corgaxm.ku_alarmy.di

import com.corgaxm.ku_alarmy.api.AuthService
import com.corgaxm.ku_alarmy.api.GradeService
import com.corgaxm.ku_alarmy.data.UseCase
import com.corgaxm.ku_alarmy.data.auth.AuthRepository
import com.corgaxm.ku_alarmy.data.auth.LoginResponse
import com.corgaxm.ku_alarmy.data.grade.GradeRepository
import com.corgaxm.ku_alarmy.data.grade.GraduationSimulationResponse
import com.corgaxm.ku_alarmy.data.grade.UserInformationResponse
import com.corgaxm.ku_alarmy.data.grade.ValidGradesResponse
import com.corgaxm.ku_alarmy.persistence.*
import com.corgaxm.ku_alarmy.utils.DateTimeConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.dsl.module

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
                val loginResponse = authService.login(username, password)

                val cookie = loginResponse.headers()["Set-Cookie"]?.split(";")?.first()
                    ?: return UseCase.error("No cookie error occurs.")
                settingsManager.setCookie(cookie)

                val loginBody = loginResponse.body()
                val loginSuccess = loginBody?.loginSuccess ?: return UseCase.error("Fail to login")
                val loginFailure = loginBody.loginFailure

                return when {
                    loginSuccess.isSucceeded -> {
                        settingsManager.setAuthInfo(username, password)
                        UseCase.success(loginBody)
                    }
                    loginFailure != null -> UseCase.error(loginFailure.errorMessage)
                    else -> UseCase.error("Error on server.")
                }
            }

            override suspend fun makeAutoLoginRequest(): UseCase<LoginResponse> {
                val username = settingsManager.usernameFlow.first()
                val password = settingsManager.passwordFlow.first()

                return when {
                    username.isBlank() || password.isBlank() -> {
                        settingsManager.setAuthInfo("", "")
                        UseCase.error("Fail to auto login.")
                    }
                    else -> {
                        val loginResponse = authService.login(username, password)

                        val cookie = loginResponse.headers()["Set-Cookie"]?.split(";")?.first()
                            ?: return UseCase.error("쿠키 없음")
                        settingsManager.setCookie(cookie)

                        val loginBody = loginResponse.body()
                        val loginSuccess =
                            loginBody?.loginSuccess ?: return UseCase.error("Fail to login")
                        val loginFailure = loginBody.loginFailure

                        when {
                            loginSuccess.isSucceeded -> UseCase.success(loginBody)
                            loginFailure != null -> UseCase.error(loginFailure.errorMessage)
                            else -> UseCase.error("Error on server.")
                        }
                    }
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
                                    evaluationMethod = grade.evaluationMethod,
                                    year = year,
                                    semester = semester,
                                    classification = grade.classification,
                                    characterGrade = grade.characterGrade,
                                    grade = grade.grade,
                                    professor = grade.professor,
                                    subjectId = grade.subjectId,
                                    subjectName = grade.subjectName,
                                    subjectNumber = grade.subjectNumber,
                                    valid = false,
                                    modifiedAt = System.currentTimeMillis()
                                )
                            }
                        }
                    }

                    gradeDao.insertGrade(*allGrades.toTypedArray())

                } catch (exception: Exception) {
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

        }
    }

    single { provideLoginRepository(get(), get()) }
    single { provideGradeRepository(get(), get(), get(), get()) }
}