package com.corgaxm.ku_alarmy.api

import com.corgaxm.ku_alarmy.data.auth.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("Login/login.do")
    suspend fun login(
        @Field(value = "SINGLE_ID") username: String,
        @Field(value = "PWD") password: String
    ): LoginResponse
}