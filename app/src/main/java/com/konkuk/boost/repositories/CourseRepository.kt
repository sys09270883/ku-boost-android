package com.konkuk.boost.repositories

import com.konkuk.boost.data.course.SyllabusDetailResponse
import com.konkuk.boost.data.course.SyllabusResponse
import com.konkuk.boost.persistence.LikeCourseEntity
import com.konkuk.boost.utils.UseCase

interface CourseRepository {
    suspend fun makeAllSyllabusRequest(year: Int, semester: Int): UseCase<SyllabusResponse>

    suspend fun makeDetailSyllabusRequest(
        year: Int,
        semester: Int,
        subjectId: String
    ): UseCase<SyllabusDetailResponse>

    fun setSemester(semester: Int): UseCase<Unit>

    fun getSemester(): UseCase<Int>

    suspend fun insertLikeCourse(
        year: Int,
        semester: Int,
        subjectId: String,
        subjectName: String,
        professor: String,
        like: Boolean
    ): UseCase<Unit>

    suspend fun makeAllLikeCoursesRequest(): UseCase<List<LikeCourseEntity>>
}