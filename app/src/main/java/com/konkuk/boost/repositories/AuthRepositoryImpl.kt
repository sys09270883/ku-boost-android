package com.konkuk.boost.repositories

import android.util.Log
import com.konkuk.boost.api.AuthService
import com.konkuk.boost.data.auth.ChangePasswordResponse
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

        /* 90일 후 변경 테스트
        val loginBody = LoginResponse(
            null, LoginFailure(
                "비밀번호 변경 후 90일이 지났습니다. 비밀번호를 변경해주세요.",
                -3000,
                "SYS.CMMN@CMMN018"
            )
        ) */

        val loginSuccess = loginBody?.loginSuccess
        val loginFailure = loginBody?.loginFailure

        if (loginSuccess == null)
            return UseCase.error("${loginFailure?.errorMessage}", loginBody)

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

    override suspend fun makeChangePasswordRequest(
        username: String,
        password: String
    ): UseCase<ChangePasswordResponse> {
        val changePasswordResponse: ChangePasswordResponse

        try {
            changePasswordResponse = authService.changePasswordAfter90Days(username, password)
        } catch (e: Exception) {
            return UseCase.error("${e.message}")
        }

        return when (changePasswordResponse.response.flag) {
            "1" -> UseCase.error("아이디, 패스워드가 일치하지 않습니다.")
            "3" -> UseCase.success(changePasswordResponse)
            "PASS" -> UseCase.success(changePasswordResponse)
            else -> UseCase.error("서버에 문제가 발생했습니다.")
        }
    }

    override suspend fun makeChangePasswordRequest(
        username: String,
        beforePassword: String,
        password: String,
        password2: String,
        procDiv: String
    ): UseCase<ChangePasswordResponse> {
        val changePasswordResponse: ChangePasswordResponse

        try {
            changePasswordResponse = authService.changePasswordAfter90Days(
                username,
                beforePassword,
                password,
                password2,
                procDiv
            )
        } catch (e: Exception) {
            return UseCase.error("${e.message}")
        }

        return when (changePasswordResponse.response.flag) {
            "1" -> UseCase.error("아이디, 패스워드가 일치하지 않습니다.")
            "3" -> UseCase.success(changePasswordResponse)
            else -> UseCase.error("서버에 문제가 발생했습니다.")
        }
    }
}