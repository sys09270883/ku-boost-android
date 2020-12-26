package com.corgaxm.ku_alarmy.data.login

import com.corgaxm.ku_alarmy.utils.Resource

interface LoginRepository {
    suspend fun makeLoginRequest(username: String, password: String): Resource<LoginResponse>
    suspend fun makeAutoLoginRequest(): Resource<LoginResponse>
}