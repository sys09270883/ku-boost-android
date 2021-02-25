package com.konkuk.boost.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.persistence.grade.GradeEntity
import com.konkuk.boost.repositories.GradeRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GraduationSimulationDetailViewModel(private val gradeRepository: GradeRepository) :
    ViewModel() {
    private val fetched = MutableLiveData(false)

    fun isFetched() = fetched.value ?: false

    var gradesByClassification = MutableLiveData<UseCase<List<GradeEntity>>>()

    private val _classification = MutableLiveData<String>()
    val classification: LiveData<String> get() = _classification

    fun setClassification(clf: String) {
        _classification.value = clf
    }

    fun fetchGradesByClassification() {
        val clf = classification.value ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (clf == "총점")
                    gradesByClassification.postValue(gradeRepository.getNotDeletedGrades())
                else
                    gradesByClassification.postValue(gradeRepository.getNotDeletedGradesByClassification(clf))
            }
            fetched.postValue(true)
        }
    }
}