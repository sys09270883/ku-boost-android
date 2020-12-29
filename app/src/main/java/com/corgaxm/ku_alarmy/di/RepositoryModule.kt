package com.corgaxm.ku_alarmy.di

import android.util.Log
import com.corgaxm.ku_alarmy.api.AuthService
import com.corgaxm.ku_alarmy.api.GradeService
import com.corgaxm.ku_alarmy.data.UseCase
import com.corgaxm.ku_alarmy.data.auth.AuthRepository
import com.corgaxm.ku_alarmy.data.auth.LoginResponse
import com.corgaxm.ku_alarmy.data.grade.GradeRepository
import com.corgaxm.ku_alarmy.data.grade.GraduationSimulationResponse
import com.corgaxm.ku_alarmy.data.grade.UserInformationResponse
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationDao
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationEntity
import com.corgaxm.ku_alarmy.persistence.SettingsManager
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
                    ?: return UseCase.error("쿠키 없음")
                settingsManager.setCookie(cookie)
                Log.d("yoonseop", "Cookie: $cookie")

                val loginBody = loginResponse.body()
                val loginSuccess = loginBody?.loginSuccess ?: return UseCase.error("로그인 실패")
                val loginFailure = loginBody.loginFailure

                return when {
                    loginSuccess.isSucceeded -> {
                        settingsManager.setAuthInfo(username, password)
                        UseCase.success(loginBody)
                    }
                    loginFailure != null -> UseCase.error(loginFailure.errorMessage)
                    else -> UseCase.error("서버 에러입니다.\n잠시 후 시도해주세요.")
                }
            }

            override suspend fun makeAutoLoginRequest(): UseCase<LoginResponse> {
                val username = settingsManager.usernameFlow.first()
                val password = settingsManager.passwordFlow.first()
                Log.d("yoonseop", "autoLogin: ($username, $password)")

                return when {
                    username.isBlank() || password.isBlank() -> {
                        settingsManager.setAuthInfo("", "")
                        UseCase.error("자동로그인 실패")
                    }
                    else -> {
                        val loginResponse = authService.login(username, password)

                        val cookie = loginResponse.headers()["Set-Cookie"]?.split(";")?.first()
                            ?: return UseCase.error("쿠키 없음")
                        settingsManager.setCookie(cookie)

                        Log.d("yoonseop", "Cookie: $cookie")

                        val loginBody = loginResponse.body()
                        val loginSuccess = loginBody?.loginSuccess ?: return UseCase.error("로그인 실패")
                        val loginFailure = loginBody.loginFailure

                        when {
                            loginSuccess.isSucceeded -> UseCase.success(loginBody)
                            loginFailure != null -> UseCase.error(loginFailure.errorMessage)
                            else -> UseCase.error("서버 에러입니다.\n잠시 후 시도해주세요.")
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
        settingsManager: SettingsManager
    ): GradeRepository {
        return object : GradeRepository {
            override suspend fun makeGraduationSimulationRequest(): UseCase<GraduationSimulationResponse> {
                val username = settingsManager.usernameFlow.first()
                val stdNo = settingsManager.stdNoFlow.first()
                val corsYy = stdNo.toString().substring(0, 4).toInt()
                val shregCd = settingsManager.codeFlow.first()

                val graduationSimulationResponse: GraduationSimulationResponse

                try {
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
                    Log.e("yoonseop", "${exception.message}")
                    return UseCase.error("졸업 시뮬레이션 API 에러 발생")
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
                    return UseCase.error("유저 정보 API 에러 발생")
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

            override suspend fun setGraduationSimulation(graduationSimulationEntity: GraduationSimulationEntity): UseCase<Unit> {
                try {
                    graduationSimulationDao.insertGraduationSimulation(graduationSimulationEntity)
                } catch (exception: Exception) {
                    return UseCase.error("졸업 시뮬레이션 저장 중 에러 발생")
                }
                return UseCase.success(Unit)
            }

            override fun getStdNoFlow(): Flow<Int> = settingsManager.stdNoFlow

        }
    }

    single { provideLoginRepository(get(), get()) }
    single { provideGradeRepository(get(), get(), get()) }
}