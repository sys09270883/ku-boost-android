package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.course.LectureInfo
import com.konkuk.boost.data.course.SyllabusResponse
import com.konkuk.boost.repositories.CourseRepository
import com.konkuk.boost.utils.DateTimeConverter
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _syllabusLoading = MutableLiveData(false)
    val syllabusLoading get() = _syllabusLoading

    private val syllabusFetched = MutableLiveData(false)

    fun isSyllabusFetched() = syllabusFetched.value ?: false

    val syllabusResponse = MutableLiveData<UseCase<SyllabusResponse>>()

    fun fetchAllSyllabus() {
        val year = _year.value ?: return
        val semester = _semester.value ?: return

        _syllabusLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                syllabusResponse.postValue(courseRepository.makeAllSyllabusRequest(year, semester))
            }
            _syllabusLoading.postValue(false)
            syllabusFetched.postValue(true)
        }
    }

    fun getFilteredList(query: String): List<LectureInfo> {
        val list = syllabusResponse.value?.data?.lectureInfoList ?: return emptyList()
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