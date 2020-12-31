package com.corgaxm.ku_alarmy.api

import com.corgaxm.ku_alarmy.data.grade.GradeResponse
import com.corgaxm.ku_alarmy.data.grade.GraduationSimulationResponse
import com.corgaxm.ku_alarmy.data.grade.UserInformationResponse
import com.corgaxm.ku_alarmy.data.grade.ValidGradesResponse
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

    @GET("GradNowShtmGradeInq/find.do?")
    suspend fun fetchRegularGrade(
        @Query("stdNo") stdNo: Int,             // ex) 201511271
        @Query("basiYy") year: Int,             // ex) 2020
        @Query("basiShtm") semester: String,    // ex) 1: 1학기 2: 2학기 4: 하계계절 5: 동계계절
        @Query("curDate") curDate: String,      // ex) 20201229
        @Query("_AUTH_MENU_KEY") authMenuKey: Int = 1140302
    ): GradeResponse

    @GET("GradGiveUpApp/findUpDown.do?")
    suspend fun fetchValidGrades(
        @Query("argStdNo") stdNo: Int,
        @Query("argYy") argYy: Int = 0,
        @Query("argShtm") argShtm: Int = 0,
        @Query("_AUTH_MENU_KEY") authMenuKey: Int = 1140606
    ): ValidGradesResponse
}