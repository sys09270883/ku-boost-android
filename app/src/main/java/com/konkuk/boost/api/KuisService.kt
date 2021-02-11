package com.konkuk.boost.api

import com.konkuk.boost.data.auth.ChangePasswordResponse
import com.konkuk.boost.data.auth.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface KuisService {
    @FormUrlEncoded
    @POST("Login/login.do")
    suspend fun login(
        @Field(value = "SINGLE_ID") username: String,
        @Field(value = "PWD") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("CmmnPwdChgPop/save.do")
    suspend fun changePasswordAfter90Days(
        @Field(value = "SINGLE_ID") username: String,
        @Field(value = "BF_PWD") beforePassword: String,
        @Field(value = "PWD") password: String = "",
        @Field(value = "PWD1") password2: String = "",
        @Field(value = "PROC_DIV") procDiv: String = "PASS",        // 90일 뒤 변경: PASS 그 외 공란
    ): ChangePasswordResponse
}