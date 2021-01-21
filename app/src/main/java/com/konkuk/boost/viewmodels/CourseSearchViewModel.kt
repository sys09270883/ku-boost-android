package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.konkuk.boost.data.course.LectureInfo
import com.konkuk.boost.repositories.CourseRepository
import com.konkuk.boost.utils.DateTimeConverter
import java.util.*

class CourseSearchViewModel(
    private val courseRepository: CourseRepository,
) : ViewModel() {
    private val _year = MutableLiveData(DateTimeConverter.currentYear().toInt())
    val year get() = _year

    fun setYear(year: String) {
        _year.value = year.toInt()
    }

    fun getYear() = year.value ?: DateTimeConverter.currentYear().toInt()

    private val _semester = MutableLiveData(courseRepository.getSemester().data ?: 1)
    val semester get() = _semester

    fun setSemester(semester: Int) {
        courseRepository.setSemester(semester)
        _semester.value = semester
    }

    fun getSemester() = semester.value ?: 1

    fun getFilteredList(list: List<LectureInfo>, query: String): List<LectureInfo> {
        val filtered = list.filter {
            it.subjectName.toLowerCase(Locale.ENGLISH)
                .contains(query.toLowerCase(Locale.ENGLISH)) || it.subjectId == query || it.dept.contains(
                query
            ) || it.professor?.toLowerCase(
                Locale.ENGLISH
            )?.contains(query.toLowerCase(Locale.ENGLISH)) ?: false
        }
        return if (filtered.size > 200) emptyList() else filtered
    }
}