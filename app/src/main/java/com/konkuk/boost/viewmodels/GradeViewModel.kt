package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.persistence.GradeEntity
import com.konkuk.boost.persistence.GraduationSimulationEntity
import com.konkuk.boost.persistence.RankEntity
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.repositories.GradeRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            withContext(Dispatchers.IO) {
                graduationSimulation.postValue(gradeRepository.getGraduationSimulations())
            }
        }
    }

//    fun fetchGraduationSimulationFromServer() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                gradeRepository.makeGraduationSimulationRequest()
//            }
//        }
//    }

    fun fetchCurrentGradesFromLocalDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // 로컬 DB에 있는 데이터를 가져와 LiveData 업데이트
                currentGrades.postValue(gradeRepository.getCurrentGrades())
            }
        }
    }

    fun fetchTotalGradesFromLocalDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // 로컬 DB에 있는 데이터를 가져와 LiveData 업데이트
                allValidGrades.postValue(gradeRepository.getAllValidGrades())
            }
        }
    }

    fun hasData(): Boolean = gradeRepository.hasData()

    val isTotalRankInsertedResponse = MutableLiveData<UseCase<Unit>>()

    val totalRankResponse = MutableLiveData<UseCase<RankEntity>>()

    fun getRankAndTotal(): Pair<Int, Int> =
        totalRankResponse.value?.data?.toRankAndTotal() ?: Pair(0, 0)

    fun makeTotalRank() {
        viewModelScope.launch {
            isTotalRankInsertedResponse.postValue(gradeRepository.makeTotalRank())
        }
    }

    fun fetchTotalRankFromLocalDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Total rank {year: 0, semester: 0}
                totalRankResponse.postValue(gradeRepository.getTotalRank(0, 0))
            }
        }
    }

    fun getDept() = authRepository.getDept()
}
