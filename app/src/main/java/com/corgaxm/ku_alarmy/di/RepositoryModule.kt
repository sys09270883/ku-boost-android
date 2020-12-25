package com.corgaxm.ku_alarmy.di

import com.corgaxm.ku_alarmy.api.LoginService
import com.corgaxm.ku_alarmy.data.login.LoginRepository
import com.corgaxm.ku_alarmy.data.login.LoginResponse
import com.corgaxm.ku_alarmy.utils.Resource
import org.koin.dsl.module

val repositoryModule = module {
    fun provideLoginRepository(loginService: LoginService): LoginRepository {
        return object : LoginRepository {
            override suspend fun makeLoginRequest(
                username: String,
                password: String
            ): Resource<LoginResponse> {
                val loginResponse = loginService.login(username, password)
                val loginSuccess = loginResponse.loginSuccess
                val loginFailure = loginResponse.loginFailure

                return when {
                    loginSuccess?.isSucceeded == true -> Resource.success(loginResponse)
                    loginFailure != null -> Resource.error(loginFailure.errorMessage)
                    else -> Resource.error("서버 에러입니다.\n잠시 후 시도해주세요.")
                }
            }
        }
    }

    single { provideLoginRepository(get()) }
}