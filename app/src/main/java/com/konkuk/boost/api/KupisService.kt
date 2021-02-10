package com.konkuk.boost.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface KupisService {
    @GET("sugang/acd/cour/aply/CourBasketInwonInq.jsp?")
    suspend fun getCourseRegistrationStatus(
        @Query("ltYy") year: Int,
        @Query("ltShtm") semester: String,
        @Query("promShyr") promiseYear: Int,
        @Query("sbjtId") subjectId: String,
        @Query("fg") fg: String = "B"
    ): ResponseBody
}