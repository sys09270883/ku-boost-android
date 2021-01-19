package com.konkuk.boost.api

import com.konkuk.boost.data.course.SyllabusResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CourseService {
    @GET("CourLectureAplyInqPop/find.do?")
    suspend fun fetchAllSyllabus(
        @Query("ltYy") year: Int,
        @Query("ltShtm") semester: String,
        @Query("_AUTH_MENU_KEY") authMenuKey: Int = 1130310
    ): SyllabusResponse
}