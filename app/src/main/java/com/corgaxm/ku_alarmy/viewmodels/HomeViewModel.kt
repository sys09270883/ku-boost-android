package com.corgaxm.ku_alarmy.viewmodels

import androidx.lifecycle.*
import com.corgaxm.ku_alarmy.data.UseCase
import com.corgaxm.ku_alarmy.data.auth.AuthRepository
import com.corgaxm.ku_alarmy.data.grade.GradeRepository
import com.corgaxm.ku_alarmy.data.grade.GraduationSimulationResponse
import com.corgaxm.ku_alarmy.persistence.GradeEntity
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val gradeRepository: GradeRepository
) : ViewModel() {
    private val _graduationSimulationLoading = MutableLiveData(false)
    val graduationSimulationLoading get() = _graduationSimulationLoading

    private val _allGradesLoading = MutableLiveData(false)
    val allGradesLoading get() = _allGradesLoading

    var logoutResponse = MutableLiveData<UseCase<Unit>>()

    var graduationSimulationResponse = MutableLiveData<UseCase<GraduationSimulationResponse>>()

    var graduationSimulation = MutableLiveData<UseCase<List<GraduationSimulationEntity>>>()

    var allValidGrades = MutableLiveData<UseCase<List<GradeEntity>>>()

    val stdNo: LiveData<Int> = gradeRepository.getStdNoFlow().asLiveData()

    fun clearLogoutResource() {
        logoutResponse = MutableLiveData<UseCase<Unit>>()
    }

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                logoutResponse.postValue(authRepository.makeLogoutRequest())
            }
        }
    }

    fun fetchGraduationSimulationFromLocalDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                graduationSimulation.postValue(gradeRepository.getGraduationSimulations())
            }
        }
    }

    fun fetchGraduationSimulationFromServer() {
        _graduationSimulationLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                graduationSimulationResponse.postValue(gradeRepository.makeGraduationSimulationRequest())
                graduationSimulation.postValue(gradeRepository.getGraduationSimulations())
            }
            _graduationSimulationLoading.postValue(false)
        }
    }

    fun fetchAllGradesFromServer() {
        // 전체 성적조회 로딩
        _allGradesLoading.value = true

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // 서버에서 전체 성적 정보를 가져와 로컬 DB에 저장
                gradeRepository.makeAllGradesRequest()
            }
            withContext(Dispatchers.IO) {
                // 서버에서 유효한 성적 정보를 가져와 로컬 DB 업데이트
                gradeRepository.makeAllValidGradesRequest()
            }
            // 로컬 DB에 있는 데이터를 가져와 LiveData 업데이트
            allValidGrades.postValue(gradeRepository.getAllValidGrades())

            // 전체 성적조회 로딩 끝
            _allGradesLoading.postValue(false)
        }
    }
}