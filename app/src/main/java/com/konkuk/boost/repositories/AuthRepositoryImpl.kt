package com.konkuk.boost.repositories

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.konkuk.boost.api.AuthorizedKuisService
import com.konkuk.boost.api.KuisService
import com.konkuk.boost.data.auth.ChangePasswordResponse
import com.konkuk.boost.data.auth.LoginResponse
import com.konkuk.boost.data.auth.StudentInfoResponse
import com.konkuk.boost.persistence.pref.PreferenceManager
import com.konkuk.boost.utils.UseCase
import retrofit2.Response

class AuthRepositoryImpl(
    private val kuisService: KuisService,
    private val preferenceManager: PreferenceManager,
    private val authorizedKuisService: AuthorizedKuisService,
) : AuthRepository {
    override suspend fun makeLoginRequest(
        username: String,
        password: String
    ): UseCase<LoginResponse> {
        val loginResponse: Response<LoginResponse>
        try {
            loginResponse = kuisService.login(username, password)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("서버에 문제가 발생했습니다.")
        }

        val cookie = loginResponse.headers()["Set-Cookie"]?.split(";")?.first()
            ?: return UseCase.error("다시 로그인하세요.")
        preferenceManager.cookie = cookie

        val loginBody = loginResponse.body()

        val loginSuccess = loginBody?.loginSuccess
        val loginFailure = loginBody?.loginFailure

        return when {
            loginSuccess?.isSucceeded == true -> {
                preferenceManager.setAuthInfo(username, password)
                UseCase.success(loginBody)
            }
            loginSuccess == null && loginFailure != null -> UseCase.error(
                loginFailure.errorMessage,
                loginBody
            )
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

    override fun getPassword() = preferenceManager.password

    override fun getName() = preferenceManager.name

    override fun getDept() = preferenceManager.dept

    override fun getStdNo() = preferenceManager.stdNo

    override fun getState() = preferenceManager.state

    override suspend fun setPassword(password: String) {
        preferenceManager.password = password
    }

    override suspend fun makeChangePasswordRequest(
        username: String,
        password: String
    ): UseCase<ChangePasswordResponse> {
        val changePasswordResponse: ChangePasswordResponse

        try {
            changePasswordResponse = kuisService.changePasswordAfter90Days(username, password)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return when (changePasswordResponse.response.flag) {
            "1" -> UseCase.error("현재 비밀번호가 일치하지 않습니다.")
            "3" -> UseCase.success(changePasswordResponse, "비밀번호를 변경했습니다.")
            "PASS" -> UseCase.success(changePasswordResponse, "90일 후 변경하기로 설정했습니다.")
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
            changePasswordResponse = kuisService.changePasswordAfter90Days(
                username,
                beforePassword,
                password,
                password2,
                procDiv
            )
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return when (changePasswordResponse.response.flag) {
            "1" -> UseCase.error("현재 비밀번호가 일치하지 않습니다.")
            "3" -> UseCase.success(changePasswordResponse, "비밀번호를 변경했습니다.")
            "PASS" -> UseCase.success(changePasswordResponse, "90일 후 변경하기로 설정했습니다.")
            else -> UseCase.error("서버에 문제가 발생했습니다.")
        }
    }

    override suspend fun makeStudentInfoRequest(): UseCase<StudentInfoResponse> {
        val stdNo = preferenceManager.stdNo

        val studentInfoResponse: StudentInfoResponse
        try {
            studentInfoResponse = authorizedKuisService.fetchStudentInfo(stdNo)
            Log.d("ku-boost", "${studentInfoResponse.deptTransferInfo}")
            Log.d("ku-boost", "${studentInfoResponse.personalInfo}")
//            Log.d("ku-boost", "${studentInfoResponse.profilePhoto}")
            Log.d("ku-boost", "${studentInfoResponse.scholarships}")
            Log.d("ku-boost", "${studentInfoResponse.studentStateChangeInfo}")
            Log.d("ku-boost", "${studentInfoResponse.tuitionFees}")
            Log.d("ku-boost", "${studentInfoResponse.warnHonors}")
        } catch (e: Exception) {
            Log.e("ku-boost", "${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(studentInfoResponse)
    }
}