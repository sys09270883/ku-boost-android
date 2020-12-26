package com.corgaxm.ku_alarmy.di

import com.corgaxm.ku_alarmy.api.CrawlService
import com.corgaxm.ku_alarmy.api.LoginService
import com.corgaxm.ku_alarmy.data.auth.AuthRepository
import com.corgaxm.ku_alarmy.data.auth.LoginResponse
import com.corgaxm.ku_alarmy.data.crawl.CrawlRepository
import com.corgaxm.ku_alarmy.data.crawl.GraduationSimulationResponse
import com.corgaxm.ku_alarmy.utils.Resource
import com.corgaxm.ku_alarmy.utils.SettingsManager
import kotlinx.coroutines.flow.first
import org.koin.dsl.module

val repositoryModule = module {
    fun provideLoginRepository(
        loginService: LoginService,
        settingsManager: SettingsManager
    ): AuthRepository {
        return object : AuthRepository {
            override suspend fun makeLoginRequest(
                username: String,
                password: String
            ): Resource<LoginResponse> {
                val loginResponse = loginService.login(username, password)
                val loginSuccess = loginResponse.loginSuccess
                val loginFailure = loginResponse.loginFailure

                return when {
                    loginSuccess?.isSucceeded == true -> {
                        settingsManager.setUserInfo(username, password)
                        Resource.success(loginResponse)
                    }
                    loginFailure != null -> Resource.error(loginFailure.errorMessage)
                    else -> Resource.error("서버 에러입니다.\n잠시 후 시도해주세요.")
                }
            }

            override suspend fun makeAutoLoginRequest(): Resource<LoginResponse> {
                val username = settingsManager.usernameFlow.first()
                val password = settingsManager.passwordFlow.first()

                return when {
                    username.isBlank() || password.isBlank() -> {
                        settingsManager.setUserInfo("", "")
                        Resource.error("자동로그인 실패")
                    }
                    else -> {
                        val loginResponse = loginService.login(username, password)
                        val loginSuccess = loginResponse.loginSuccess
                        val loginFailure = loginResponse.loginFailure

                        when {
                            loginSuccess?.isSucceeded == true -> Resource.success(loginResponse)
                            loginFailure != null -> Resource.error(loginFailure.errorMessage)
                            else -> Resource.error("서버 에러입니다.\n잠시 후 시도해주세요.")
                        }
                    }
                }
            }

            override suspend fun makeLogoutRequest(): Resource<Unit> {
                settingsManager.setUserInfo("", "")
                return Resource.success(Unit)
            }
        }
    }

    fun provideCrawlRepository(
        crawlService: CrawlService,
        settingsManager: SettingsManager
    ): CrawlRepository {
        return object : CrawlRepository {
            override suspend fun makeGraduationSimulationRequest(): Resource<GraduationSimulationResponse> {
                val username = settingsManager.usernameFlow.first()
                val password = settingsManager.passwordFlow.first()

                val graduationSimulationResponse: GraduationSimulationResponse

                try {
                    graduationSimulationResponse =
                        crawlService.fetchGraduationSimulation(username, password)
                } catch (exception: Exception) {
                    return Resource.error("크롤링 중 에러 발생")
                }

                return when (graduationSimulationResponse.responseCode) {
                    201 -> Resource.success(graduationSimulationResponse)
                    else -> Resource.error("졸업 시뮬레이션 크롤링 실패")
                }
            }
        }
    }

    single { provideLoginRepository(get(), get()) }
    single { provideCrawlRepository(get(), get()) }
}