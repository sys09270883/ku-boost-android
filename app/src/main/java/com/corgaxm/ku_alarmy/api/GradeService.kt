package com.corgaxm.ku_alarmy.api

import com.corgaxm.ku_alarmy.data.grade.GraduationSimulationResponse
import com.corgaxm.ku_alarmy.data.grade.UserInformationResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GradeService {
    @GET("GrdtStdSimul/findSearch2.do?")
    suspend fun fetchGraduationSimulation(
        @Query("shregCd") shregCd: String,
        @Query("corsYy") year: Int,
        @Query("stdNo") stdNo: Int,
        @Query("_AUTH_MENU_KEY") authMenuKey: Int = 1170201
    ): GraduationSimulationResponse

    @POST("Main/onLoad.do")
    suspend fun fetchUserInformation(): UserInformationResponse
}