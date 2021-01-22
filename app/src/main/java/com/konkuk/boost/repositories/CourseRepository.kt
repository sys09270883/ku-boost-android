package com.konkuk.boost.repositories

import com.konkuk.boost.data.course.SyllabusDetailResponse
import com.konkuk.boost.data.course.SyllabusResponse
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
}