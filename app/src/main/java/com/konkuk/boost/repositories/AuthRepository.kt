package com.konkuk.boost.repositories

import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.data.auth.LoginResponse

interface AuthRepository {
    suspend fun makeLoginRequest(username: String, password: String): UseCase<LoginResponse>

    suspend fun makeAutoLoginRequest(): UseCase<LoginResponse>

    suspend fun makeLogoutRequest(): UseCase<Unit>

    fun getUsername(): String
}