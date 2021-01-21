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

    fun fetchDetailSyllabus() {
        val subjectId = subjectId.value ?: return
        val year = _year.value ?: return
        val semester = _semester.value ?: return

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

    fun getBookList() = detailSyllabusResponse.value?.data?.book ?: emptyList()

    fun getWorkList() = detailSyllabusResponse.value?.data?.work ?: emptyList()
}