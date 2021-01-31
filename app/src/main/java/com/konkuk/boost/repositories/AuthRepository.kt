package com.konkuk.boost.repositories

import com.konkuk.boost.data.auth.ChangePasswordResponse
import com.konkuk.boost.data.auth.LoginResponse
import com.konkuk.boost.utils.UseCase

interface AuthRepository {
    suspend fun makeLoginRequest(username: String, password: String): UseCase<LoginResponse>

    suspend fun makeAutoLoginRequest(): UseCase<LoginResponse>

    suspend fun makeLogoutRequest(): UseCase<Unit>

    fun getUsername(): String

    fun getName(): String

    fun getDept(): String

    fun getStdNo(): Int

    suspend fun makeChangePasswordRequest(
        username: String,
        password: String
    ): UseCase<ChangePasswordResponse>

    suspend fun makeChangePasswordRequest(
        username: String,
        beforePassword: String,
        password: String,
        password2: String,
        procDiv: String = ""
    ): UseCase<ChangePasswordResponse>
}