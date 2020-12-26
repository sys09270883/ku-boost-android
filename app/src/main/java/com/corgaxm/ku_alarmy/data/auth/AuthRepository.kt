package com.corgaxm.ku_alarmy.data.auth

import com.corgaxm.ku_alarmy.utils.Resource

interface AuthRepository {
    suspend fun makeLoginRequest(username: String, password: String): Resource<LoginResponse>
    suspend fun makeAutoLoginRequest(): Resource<LoginResponse>
    suspend fun makeLogoutRequest(): Resource<Unit>
}