package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.course.RegistrationStatus
import com.konkuk.boost.data.course.RegistrationStatusData
import com.konkuk.boost.data.course.SyllabusResponse
import com.konkuk.boost.persistence.like.LikeCourseEntity
import com.konkuk.boost.repositories.CourseRepository
import com.konkuk.boost.utils.DateTimeConverter
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

    fun fetchAllSyllabus() {
        val year = selectedYear.value ?: DateTimeConverter.currentYear().toInt()
        val semester = selectedSemester.value ?: 1

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

    val courseAndRegistrationStatus = MutableLiveData<List<RegistrationStatusData>>()

    fun fetchAllLikeCourses() {
        val year = selectedYear.value ?: DateTimeConverter.currentYear().toInt()
        val semester = selectedSemester.value ?: 1

        viewModelScope.launch {
            allLikeCoursesResponse.postValue(
                courseRepository.makeAllLikeCoursesRequest(
                    year,
                    semester
                )
            )

            registrationStatusListResponse.postValue(
                courseRepository.makeCourseRegistrationStatusRequest(
                    year,
                    semester,
                    getAllLikeCoursesId()
                )
            )

            val list = mutableListOf<RegistrationStatusData>()
            val allLikeCourses = allLikeCoursesResponse.value?.data ?: return@launch
            val registrationStatusList =
                registrationStatusListResponse.value?.data ?: return@launch

            if (allLikeCourses.size != registrationStatusList.size)
                return@launch

            for (idx in allLikeCourses.indices) {
                list += RegistrationStatusData(
                    allLikeCourses[idx],
                    registrationStatusList[idx],
                )
            }

            courseAndRegistrationStatus.postValue(list)
        }
    }

    // course id list 가져옴
    private fun getAllLikeCoursesId(): List<String> {
        val list = allLikeCoursesResponse.value?.data ?: emptyList()
        val subjectIdList = mutableListOf<String>()

        for (item in list) {
            subjectIdList.add(item.subjectId)
        }

        return subjectIdList
    }

    val registrationStatusListResponse = MutableLiveData<UseCase<List<List<RegistrationStatus>>>>()

    fun fetchRegistrationStatus() {
        val year = selectedYear.value ?: DateTimeConverter.currentYear().toInt()
        val semester = selectedSemester.value ?: 1

        viewModelScope.launch {
            registrationStatusListResponse.postValue(
                courseRepository.makeCourseRegistrationStatusRequest(
                    year,
                    semester,
                    getAllLikeCoursesId()
                )
            )
        }
    }

    val selectedYear = MutableLiveData(
        DateTimeConverter.currentYear().toInt()
    )

    val selectedSemester = MutableLiveData(
        courseRepository.getSemester().data ?: 1
    )

    fun setSemester(semester: Int) {
        courseRepository.setSemester(semester)
    }

    fun updateSemester(semester: Int) {
        selectedSemester.value = semester
    }

    fun fetchSelectedSemester() {
        viewModelScope.launch {
            selectedSemester.postValue(courseRepository.getSemester().data ?: 1)
        }
    }

}