package com.konkuk.boost.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.konkuk.boost.data.grade.ParcelableGrade

class GradeDetailViewModel : ViewModel() {

    private val _year = MutableLiveData<Int>()
    val year get(): LiveData<Int> = _year

    private val _semester = MutableLiveData<String>()
    val semester: LiveData<String> get() = _semester

    private val _characterGrade = MutableLiveData<String>()
    val characterGrade: LiveData<String> get() = _characterGrade

    private val _subjectId = MutableLiveData<String>()
    val subjectId: LiveData<String> get() = _subjectId

    private val _subjectName = MutableLiveData<String>()
    val subjectName: LiveData<String> get() = _subjectName

    private val _subjectNumber = MutableLiveData<String>()
    val subjectNumber: LiveData<String> get() = _subjectNumber

    private val _subjectPoint = MutableLiveData<Int>()
    val subjectPoint: LiveData<Int> get() = _subjectPoint

    private val _professor = MutableLiveData<String>()
    val professor: LiveData<String> get() = _professor

    private val _grade = MutableLiveData<Float>()
    val grade: LiveData<Float> get() = _grade

    private val _evaluationMethod = MutableLiveData<String>()
    val evaluationMethod: LiveData<String> get() = _evaluationMethod

    private val _classification = MutableLiveData<String>()
    val classification: LiveData<String> get() = _classification

    fun setGrade(grade: ParcelableGrade?) {
        grade?.let {
            _year.postValue(grade.year)
            _semester.postValue(grade.semester)
            _characterGrade.postValue(grade.characterGrade)
            _subjectId.postValue(grade.subjectId)
            _subjectName.postValue(grade.subjectName)
            _subjectNumber.postValue(grade.subjectNumber)
            _subjectPoint.postValue(grade.subjectPoint)
            _professor.postValue(grade.professor)
            _grade.postValue(grade.grade)
            _evaluationMethod.postValue(grade.evaluationMethod)
            _classification.postValue(grade.classification)
        }
    }
}