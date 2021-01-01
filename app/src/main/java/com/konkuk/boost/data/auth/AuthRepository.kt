package com.konkuk.boost.data.auth

import com.konkuk.boost.data.UseCase

interface AuthRepository {
    suspend fun makeLoginRequest(username: String, password: String): UseCase<LoginResponse>

    suspend fun makeAutoLoginRequest(): UseCase<LoginResponse>

    suspend fun makeLogoutRequest(): UseCase<Unit>
}