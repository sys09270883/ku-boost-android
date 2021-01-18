package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.course.LectureInfo
import com.konkuk.boost.data.course.SyllabusResponse
import com.konkuk.boost.repositories.CourseRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CourseViewModel(
    private val courseRepository: CourseRepository,
) : ViewModel() {

    private val _syllabusLoading = MutableLiveData(false)
    val syllabusLoading get() = _syllabusLoading

    private val syllabusFetched = MutableLiveData(false)

    fun isSyllabusFetched() = syllabusFetched.value ?: false

    val syllabusResponse = MutableLiveData<UseCase<SyllabusResponse>>()

    fun fetchAllSyllabus(year: Int, semester: Int) {
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
                .contains(query.toLowerCase(Locale.ENGLISH)) || it.subjectId.contains(query) || it.professor?.toLowerCase(
                Locale.ENGLISH
            )?.contains(query.toLowerCase(Locale.ENGLISH)) ?: false
        }
        return if (filtered.size > 100) emptyList() else filtered
    }

}