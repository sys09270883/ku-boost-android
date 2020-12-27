package com.corgaxm.ku_alarmy.di

import android.util.Log
import com.corgaxm.ku_alarmy.api.CrawlService
import com.corgaxm.ku_alarmy.api.LoginService
import com.corgaxm.ku_alarmy.data.auth.AuthRepository
import com.corgaxm.ku_alarmy.data.auth.LoginResponse
import com.corgaxm.ku_alarmy.data.crawl.CrawlRepository
import com.corgaxm.ku_alarmy.data.crawl.GraduationSimulationResponse
import com.corgaxm.ku_alarmy.data.db.GradeRepository
import com.corgaxm.ku_alarmy.data.db.GraduationSimulationDao
import com.corgaxm.ku_alarmy.data.db.GraduationSimulationData
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
        settingsManager: SettingsManager,
        graduationSimulationDao: GraduationSimulationDao
    ): CrawlRepository {
        return object : CrawlRepository {
            override suspend fun makeGraduationSimulationRequest(): Resource<GraduationSimulationResponse> {
                val username = settingsManager.usernameFlow.first()
                val password = settingsManager.passwordFlow.first()

                val graduationSimulationResponse: GraduationSimulationResponse

                try {
                    graduationSimulationResponse =
                        crawlService.fetchGraduationSimulation(username, password)

                    // DB(ROOM)에 저장
                    val std = graduationSimulationResponse.graduationSimulation.standard
                    val acq = graduationSimulationResponse.graduationSimulation.acquired
                    val standard =
                        GraduationSimulationData(
                            username = username,
                            basicElective = std.basicElective,
                            advancedElective = std.advancedElective,
                            generalElective = std.generalElective,
                            coreElective = std.coreElective,
                            normalElective = std.normalElective,
                            generalRequirement = std.generalRequirement,
                            majorRequirement = std.majorRequirement,
                            majorElective = std.majorElective,
                            dualElective = std.dualElective,
                            dualRequirement = std.dualRequirement,
                            dualMajorElective = std.dualMajorElective,
                            etc = std.etc,
                            type = "standard",
                            createdAt = System.currentTimeMillis(),
                            modifiedAt = System.currentTimeMillis(),
                        )
                    val acquired =
                        GraduationSimulationData(
                            username = username,
                            basicElective = acq.basicElective,
                            advancedElective = acq.advancedElective,
                            generalElective = acq.generalElective,
                            coreElective = acq.coreElective,
                            normalElective = acq.normalElective,
                            generalRequirement = acq.generalRequirement,
                            majorRequirement = acq.majorRequirement,
                            majorElective = acq.majorElective,
                            dualElective = acq.dualElective,
                            dualRequirement = acq.dualRequirement,
                            dualMajorElective = acq.dualMajorElective,
                            etc = acq.etc,
                            type = "acquired",
                            createdAt = System.currentTimeMillis(),
                            modifiedAt = System.currentTimeMillis(),
                        )

                    graduationSimulationDao.insertGraduationSimulation(standard, acquired)
                    Log.d("yoonseop", "standard: $standard")
                    Log.d("yoonseop", "acquired: $acquired")
                    Log.d("yoonseop", "db 저장 성공")

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

    fun provideGradeRepository(
        graduationSimulationDao: GraduationSimulationDao,
        settingsManager: SettingsManager
    ): GradeRepository {
        return object : GradeRepository {
            override suspend fun setGraduationSimulations(
                standard: GraduationSimulationData,
                acquired: GraduationSimulationData
            ): Resource<Unit> {
                try {
                    graduationSimulationDao.insertGraduationSimulation(standard, acquired)
                } catch (exception: Exception) {
                    return Resource.error("졸업 시뮬레이션 저장 중 에러 발생")
                }
                return Resource.success(Unit)
            }

            override suspend fun getGraduationSimulations(): Resource<List<GraduationSimulationData>> {
                val username = settingsManager.usernameFlow.first()
                var graduationSimulationList: List<GraduationSimulationData> = emptyList()

                try {
                    graduationSimulationList =
                        graduationSimulationDao.loadGraduationSimulationByUsername(username).first()
                } catch (exception: Exception) {
                    Log.d("yoonseop", "DB: 가져올 때 에러 발생")
                    return Resource.error("${exception.message}")
                }

                return Resource.success(graduationSimulationList)
            }

        }
    }

    single { provideLoginRepository(get(), get()) }
    single { provideCrawlRepository(get(), get(), get()) }
    single { provideGradeRepository(get(), get()) }
}