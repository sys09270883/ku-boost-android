package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.persistence.grade.GradeEntity
import com.konkuk.boost.persistence.rank.RankEntity
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.repositories.GradeRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.launch

class TotalGradeViewModel(
    private val authRepository: AuthRepository,
    private val gradeRepository: GradeRepository
) : ViewModel() {

    private val fetched = MutableLiveData(false)

    private val selectedPosition = MutableLiveData(0)

    var allValidGrades = MutableLiveData<UseCase<List<GradeEntity>>>()

    fun fetchAllGradesFromLocal() {
        viewModelScope.launch {
            allValidGrades.postValue(gradeRepository.getAllValidGrades())
            fetched.postValue(true)
        }
    }

    fun isFetched(): Boolean = fetched.value ?: false

    fun setSelectedPosition(position: Int) {
        selectedPosition.postValue(position)
    }

    fun getSelectedPosition() = selectedPosition.value ?: 0

    val selectedRankResponse = MutableLiveData<UseCase<RankEntity>>()

    fun getRankAndTotal(): Pair<Int, Int> =
        selectedRankResponse.value?.data?.toRankAndTotal() ?: Pair(0, 0)

    fun fetchSelectedRankFromLocalDb(year: Int, semester: Int) {
        viewModelScope.launch {
            // Total rank {year: 0, semester: 0}
            selectedRankResponse.postValue(gradeRepository.getTotalRank(year, semester))
        }
    }

    private val dept = MutableLiveData(authRepository.getDept())

    fun getDept() = dept.value ?: ""
}