package com.konkuk.boost.repositories

import android.util.Log
import com.konkuk.boost.api.AuthService
import com.konkuk.boost.data.auth.LoginResponse
import com.konkuk.boost.persistence.PreferenceManager
import com.konkuk.boost.utils.UseCase
import retrofit2.Response

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val preferenceManager: PreferenceManager
) : AuthRepository {
    override suspend fun makeLoginRequest(
        username: String,
        password: String
    ): UseCase<LoginResponse> {
        val loginResponse: Response<LoginResponse>
        try {
            loginResponse = authService.login(username, password)
        } catch (e: Exception) {
            Log.e("ku-boost", "${e.message}")
            return UseCase.error("서버에 문제가 발생했습니다.")
        }

        val cookie = loginResponse.headers()["Set-Cookie"]?.split(";")?.first()
            ?: return UseCase.error("다시 로그인하세요.")
        preferenceManager.cookie = cookie

        val loginBody = loginResponse.body()
        val loginSuccess =
            loginBody?.loginSuccess ?: return UseCase.error("아이디, 비밀번호를 확인하세요.")
        val loginFailure = loginBody.loginFailure

        return when {
            loginSuccess.isSucceeded -> {
                preferenceManager.setAuthInfo(username, password)
                UseCase.success(loginBody)
            }
            loginFailure != null -> UseCase.error(loginFailure.errorMessage)
            else -> UseCase.error("서버에 문제가 발생했습니다.")
        }
    }

    override suspend fun makeAutoLoginRequest(): UseCase<LoginResponse> {
        val username = preferenceManager.username
        val password = preferenceManager.password
        return makeLoginRequest(username, password)
    }

    override suspend fun makeLogoutRequest(): UseCase<Unit> {
        preferenceManager.clearAll()
        return UseCase.success(Unit)
    }

    override fun getUsername() = preferenceManager.username

    override fun getName() = preferenceManager.name

    override fun getDept() = preferenceManager.dept

    override fun getStdNo() = preferenceManager.stdNo
}