package com.konkuk.boost

import com.konkuk.boost.data.auth.LoginFailure
import com.konkuk.boost.data.auth.LoginResponse
import com.konkuk.boost.data.auth.LoginSuccess
import org.junit.Test

class LoginUnitTest {

    @Test
    fun `login_isSuccess`() {
        val loginSuccessResponse = LoginResponse(
            LoginSuccess(
                true
            ), null
        )

        val loginSuccess = loginSuccessResponse.loginSuccess
        val loginFailure = loginSuccessResponse.loginFailure

        assert(loginSuccess != null)
        assert(loginFailure == null)
        assert(loginSuccess?.isSucceeded == true)
    }

    @Test
    fun `login_changePasswordAfter90Days`() {
        val loginFailedResponse = LoginResponse(
            null, LoginFailure(
                "비밀번호 변경 후 90일이 지났습니다. 비밀번호를 변경해주세요.",
                -3000,
                "SYS.CMMN@CMMN018"
            )
        )

        val loginSuccess = loginFailedResponse.loginSuccess
        val loginFailure = loginFailedResponse.loginFailure

        assert(loginSuccess == null)
        assert(loginFailure != null)
        assert(loginFailure?.errorCode == "SYS.CMMN@CMMN018")
    }

    @Test
    fun `login_invalidPassword`() {
        val loginFailedResponse = LoginResponse(
            null, LoginFailure(
                "로그인 실패하였습니다.(1회)\n*5회이상 실패시 접속불가*",
                -3000,
                "로그인 실패하였습니다.(1회)\\n*5회이상 실패시 접속불가*"
            )
        )

        val loginSuccess = loginFailedResponse.loginSuccess
        val loginFailure = loginFailedResponse.loginFailure

        assert(loginSuccess == null)
        assert(loginFailure != null)
        assert(loginFailure!!.errorCode.contains("로그인 실패하였습니다."))
    }
}