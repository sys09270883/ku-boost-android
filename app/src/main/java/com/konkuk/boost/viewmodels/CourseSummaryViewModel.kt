package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.course.SyllabusDetailResponse
import com.konkuk.boost.repositories.CourseRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CourseSummaryViewModel(
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val _subjectId = MutableLiveData("")
    val subjectId get() = _subjectId

    fun setSubjectId(sbjtId: String) {
        _subjectId.value = sbjtId
    }

    private val _year = MutableLiveData(2021)
    val year get() = _year

    fun setYear(year: Int) {
        _year.value = year
    }

    private val _semester = MutableLiveData(1)
    val semester get() = _semester

    fun setSemester(semester: Int) {
        _semester.value = semester
    }

    val detailSyllabusResponse = MutableLiveData<UseCase<SyllabusDetailResponse>>()

    fun fetchDetailSyllabus(year: Int, semester: Int) {
        val subjectId = subjectId.value ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                detailSyllabusResponse.postValue(
                    courseRepository.makeDetailSyllabusRequest(
                        year,
                        semester,
                        subjectId
                    )
                )
            }
        }
    }
}