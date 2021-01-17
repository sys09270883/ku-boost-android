package com.konkuk.boost.repositories

import com.konkuk.boost.data.course.SyllabusResponse
import com.konkuk.boost.utils.UseCase

interface CourseRepository {
    suspend fun makeAllSyllabusRequest(year: Int, semester: Int): UseCase<SyllabusResponse>
}