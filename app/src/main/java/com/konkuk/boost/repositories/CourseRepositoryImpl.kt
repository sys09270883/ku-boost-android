package com.konkuk.boost.repositories

import android.util.Log
import com.konkuk.boost.api.CourseService
import com.konkuk.boost.data.course.SyllabusDetailResponse
import com.konkuk.boost.data.course.SyllabusResponse
import com.konkuk.boost.persistence.PreferenceManager
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.UseCase

class CourseRepositoryImpl(
    private val courseService: CourseService,
    private val preferenceManager: PreferenceManager
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

    override suspend fun makeDetailSyllabusRequest(
        year: Int,
        semester: Int,
        subjectId: String
    ): UseCase<SyllabusDetailResponse> {
        val syllabusDetailResponse: SyllabusDetailResponse
        try {
            syllabusDetailResponse = courseService.fetchSyllabus(
                year,
                GradeUtils.convertToSemesterCode(semester),
                subjectId
            )
            Log.d("ku-boost", "Syllabus detail: $syllabusDetailResponse")
        } catch (e: Exception) {
            Log.e("ku-boost", "${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(syllabusDetailResponse)
    }

    override fun setSemester(semester: Int): UseCase<Unit> {
        try {
            preferenceManager.selectedSemester = semester
        } catch (e: Exception) {
            return UseCase.error("${e.message}")
        }
        return UseCase.success(Unit)
    }

    override fun getSemester(): UseCase<Int> {
        return UseCase.success(preferenceManager.selectedSemester)
    }
}