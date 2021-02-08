package com.konkuk.boost.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OzService {
    @POST("oz70/server")
    @Headers("Content-Type: application/octet-stream")
    suspend fun postOzBinary(@Body file: RequestBody): ResponseBody
}