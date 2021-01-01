package com.konkuk.boost.api

import com.konkuk.boost.data.auth.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("Login/login.do")
    suspend fun login(
        @Field(value = "SINGLE_ID") username: String,
        @Field(value = "PWD") password: String
    ): Response<LoginResponse>
}