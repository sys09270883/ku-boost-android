package com.corgaxm.ku_alarmy.data.auth

import com.corgaxm.ku_alarmy.data.UseCase

interface AuthRepository {
    suspend fun makeLoginRequest(username: String, password: String): UseCase<LoginResponse>

    suspend fun makeAutoLoginRequest(): UseCase<LoginResponse>

    suspend fun makeLogoutRequest(): UseCase<Unit>
}