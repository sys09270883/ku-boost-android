package com.konkuk.boost.api

import com.konkuk.boost.data.course.SyllabusDetailResponse
import com.konkuk.boost.data.course.SyllabusResponse
import com.konkuk.boost.data.grade.GradeResponse
import com.konkuk.boost.data.grade.GraduationSimulationResponse
import com.konkuk.boost.data.grade.UserInformationResponse
import com.konkuk.boost.data.grade.ValidGradesResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthorizedKuisService {
    @GET("CourLectureAplyInqPop/find.do?")
    suspend fun fetchAllSyllabus(
        @Query("ltYy") year: Int,
        @Query("ltShtm") semester: String,
        @Query("_AUTH_MENU_KEY") authMenuKey: Int = 1130310
    ): SyllabusResponse

    @GET("CourLecturePlanInqNew/find.do?")
    suspend fun fetchSyllabus(
        @Query("argLtYy") year: Int,
        @Query("argLtShtm") semester: String,
        @Query("argSbjtId") subjectId: String,
        @Query("_AUTH_MENU_KEY") authMenuKey: Int = 1130310
    ): SyllabusDetailResponse

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