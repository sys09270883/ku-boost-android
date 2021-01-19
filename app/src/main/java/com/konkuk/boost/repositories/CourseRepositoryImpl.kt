package com.konkuk.boost.repositories

import com.konkuk.boost.api.CourseService
import com.konkuk.boost.data.course.SyllabusResponse
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.UseCase

class CourseRepositoryImpl(
    private val courseService: CourseService
) : CourseRepository {
    override suspend fun makeAllSyllabusRequest(
        year: Int,
        semester: Int
    ): UseCase<SyllabusResponse> {
        val syllabusResponse: SyllabusResponse
        try {
            syllabusResponse =
                courseService.fetchAllSyllabus(year, GradeUtils.convertToSemesterCode(semester))
        } catch (e: Exception) {
            return UseCase.error("${e.message}")
        }

        return UseCase.success(syllabusResponse)
    }
}