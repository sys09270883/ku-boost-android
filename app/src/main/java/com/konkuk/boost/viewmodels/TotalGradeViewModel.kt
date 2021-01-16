package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.persistence.GradeEntity
import com.konkuk.boost.repositories.GradeRepository
import com.konkuk.boost.utils.UseCase
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
            withContext(Dispatchers.IO) {
                // 로컬 DB에 있는 데이터를 가져와 LiveData 업데이트
                allValidGrades.postValue(gradeRepository.getAllValidGrades())
            }

            fetched.postValue(true)
        }
    }

    fun isFetched(): Boolean = fetched.value ?: false

    fun setSelectedPosition(position: Int) {
        selectedPosition.postValue(position)
    }

    fun getSelectedPosition() = selectedPosition.value ?: 0
}