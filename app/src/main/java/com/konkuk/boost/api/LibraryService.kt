package com.konkuk.boost.api

import com.konkuk.boost.data.library.LoginRequest
import com.konkuk.boost.data.library.LoginResponse
import com.konkuk.boost.data.library.QRResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LibraryService {
    @POST("pyxis-api/api/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("pyxis-api/9/api/my-membership-card")
    suspend fun getMobileQRCode(@Header("pyxis-auth-token") accessToken: String): QRResponse
}