package com.corgaxm.ku_alarmy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corgaxm.ku_alarmy.data.UseCase
import com.corgaxm.ku_alarmy.data.grade.GradeRepository
import com.corgaxm.ku_alarmy.persistence.GradeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TotalGradeViewModel(private val gradeRepository: GradeRepository): ViewModel() {

    private val fetched = MutableLiveData(false)

    private val selectedPosition = MutableLiveData(0)

    var allValidGrades = MutableLiveData<UseCase<List<GradeEntity>>>()

    fun fetchAllGradesFromLocal() {
        // 전체 성적조회 로딩

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // 서버에서 유효한 성적 정보를 가져와 로컬 DB 업데이트
                gradeRepository.makeAllValidGradesRequest()
            }
            // 로컬 DB에 있는 데이터를 가져와 LiveData 업데이트
            allValidGrades.postValue(gradeRepository.getAllValidGrades())

            fetched.postValue(true)
            // 전체 성적조회 로딩 끝
        }
    }

    fun setFetched(isFetched: Boolean) {
        fetched.postValue(isFetched)
    }

    fun isFetched(): Boolean = fetched.value ?: true

    fun setSelectedPosition(position: Int) {
        selectedPosition.postValue(position)
    }

    fun getSelectedPosition() = selectedPosition.value ?: 0
}