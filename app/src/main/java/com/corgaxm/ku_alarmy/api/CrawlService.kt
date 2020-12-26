package com.corgaxm.ku_alarmy.api

import com.corgaxm.ku_alarmy.data.crawl.GraduationSimulationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CrawlService {
    @FormUrlEncoded
    @POST("api/crawl/graduation-simulation/")
    suspend fun fetchGraduationSimulation(
        @Field(value = "username") username: String,
        @Field(value = "password") password: String
    ): GraduationSimulationResponse
}