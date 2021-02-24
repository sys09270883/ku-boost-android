package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.course.SyllabusDetailResponse
import com.konkuk.boost.persistence.like.LikeCourseEntity
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

    private val _subjectName = MutableLiveData("")
    val subjectName get() = _subjectName

    fun setSubjectName(sbjtName: String) {
        _subjectName.value = sbjtName
    }

    private val _professor = MutableLiveData("")
    val professor get() = _professor

    fun setProfessor(prof: String) {
        _professor.value = prof
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

    fun getWeekPlanList() = detailSyllabusResponse.value?.data?.weekPlan ?: emptyList()

    fun updateLikeCourse(like: Boolean) {
        val year = _year.value ?: return
        val semester = _semester.value ?: return
        val subjectId = _subjectId.value ?: return
        val subjectName = _subjectName.value ?: return
        val professor = _professor.value ?: return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                courseRepository.insertLikeCourse(
                    year,
                    semester,
                    subjectId,
                    subjectName,
                    professor,
                    like
                )
            }
        }
    }

    val like = MutableLiveData<Boolean>(false)

    fun setLike(_like: Boolean) {
        like.value = _like
    }

    fun getLike() = like.value ?: false

    val isLikeResponse = MutableLiveData<UseCase<LikeCourseEntity?>>()

    fun fetchLikeCourseExists() {
        val year = _year.value ?: return
        val semester = _semester.value ?: return
        val subjectId = _subjectId.value ?: return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                isLikeResponse.postValue(courseRepository.isExist(year, semester, subjectId))
            }
        }
    }
}