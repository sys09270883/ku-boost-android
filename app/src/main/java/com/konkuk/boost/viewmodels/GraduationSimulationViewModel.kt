package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.grade.SubjectAreaCount
import com.konkuk.boost.persistence.simul.GraduationSimulationEntity
import com.konkuk.boost.repositories.GradeRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GraduationSimulationViewModel(private val gradeRepository: GradeRepository) : ViewModel() {

    val stdNo = MutableLiveData(gradeRepository.getStdNo())

    fun getCoreFlag(): Int {
        val year = stdNo.value?.let {
            it / 100_000
        } ?: return 0

        return if (year <= 2015) 2 else 3
    }

    var graduationSimulation = MutableLiveData<UseCase<List<GraduationSimulationEntity>>>()

    fun fetchGraduationSimulationFromLocalDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                graduationSimulation.postValue(gradeRepository.getGraduationSimulations())
            }
        }
    }

    val subjectAreaCounts = MutableLiveData<UseCase<List<SubjectAreaCount>>>()

    fun fetchElectiveStatus() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                subjectAreaCounts.postValue(gradeRepository.getSubjectAreaCounts())
            }
        }
    }

    fun getAreaWithCounts() =
        subjectAreaCounts.value?.data ?: throw Exception("subjectAreaCounts are empty.")
}