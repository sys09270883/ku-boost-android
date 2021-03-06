package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.persistence.grade.GradeEntity
import com.konkuk.boost.persistence.rank.RankEntity
import com.konkuk.boost.persistence.simul.GraduationSimulationEntity
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.repositories.GradeRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.launch

class GradeViewModel(
    private val authRepository: AuthRepository,
    private val gradeRepository: GradeRepository
) : ViewModel() {
    val fetched = MutableLiveData(false)

    fun isFetched() = fetched.value == true

    fun setFetch(value: Boolean) {
        fetched.value = value
    }

    var logoutResponse = MutableLiveData<UseCase<Unit>>()

    var graduationSimulation = MutableLiveData<UseCase<List<GraduationSimulationEntity>>>()

    var allValidGrades = MutableLiveData<UseCase<List<GradeEntity>>>()

    var currentGrades = MutableLiveData<UseCase<List<GradeEntity>>>()

    val stdNo: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().apply {
            postValue(gradeRepository.getStdNo())
        }
    }

    fun clearLogoutResource() {
        logoutResponse = MutableLiveData<UseCase<Unit>>()
    }

    fun fetchGraduationSimulationFromLocalDb() {
        viewModelScope.launch {
            graduationSimulation.postValue(gradeRepository.getGraduationSimulations())
        }
    }

    fun fetchCurrentGradesFromLocalDb() {
        viewModelScope.launch {
            currentGrades.postValue(gradeRepository.getCurrentGrades())
        }
    }

    fun fetchTotalGradesFromLocalDb() {
        viewModelScope.launch {
            allValidGrades.postValue(gradeRepository.getAllValidGrades())
        }
    }

    fun hasData(): Boolean = gradeRepository.hasData()

    val totalRankResponse = MutableLiveData<UseCase<RankEntity>>()

    fun getRankAndTotal(): Pair<Int, Int> =
        totalRankResponse.value?.data?.toRankAndTotal() ?: Pair(0, 0)

    fun fetchTotalRankFromLocalDb() {
        viewModelScope.launch {
            // Total rank {year: 0, semester: 0}
            totalRankResponse.postValue(gradeRepository.getTotalRank(0, 0))
        }
    }

    fun getDept() = authRepository.getDept()
}
