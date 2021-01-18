package com.konkuk.boost.api

import com.konkuk.boost.data.library.LoginRequest
import com.konkuk.boost.data.library.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LibraryService {
    @POST("pyxis-api/api/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

//    suspend fun getMobileQRCode()
}