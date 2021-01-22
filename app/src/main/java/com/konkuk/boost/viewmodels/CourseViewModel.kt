package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.course.SyllabusResponse
import com.konkuk.boost.persistence.LikeCourseEntity
import com.konkuk.boost.repositories.CourseRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CourseViewModel(
    private val courseRepository: CourseRepository,
) : ViewModel() {

    val isFabOpened = MutableLiveData(false)

    fun setFabOpened(isOpened: Boolean) {
        isFabOpened.value = isOpened
    }

    fun isFabOpened() = isFabOpened.value ?: false

    private val syllabusFetched = MutableLiveData(false)

    fun isSyllabusFetched() = syllabusFetched.value ?: false

    private val _syllabusLoading = MutableLiveData(false)
    val syllabusLoading get() = _syllabusLoading

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

    fun getSyllabusList() = syllabusResponse.value?.data?.lectureInfoList ?: emptyList()

    val allLikeCoursesResponse = MutableLiveData<UseCase<List<LikeCourseEntity>>>()

    fun fetchAllLikeCourses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                allLikeCoursesResponse.postValue(courseRepository.makeAllLikeCoursesRequest())
            }
        }
    }

}