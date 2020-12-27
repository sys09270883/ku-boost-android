package com.corgaxm.ku_alarmy.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corgaxm.ku_alarmy.data.auth.AuthRepository
import com.corgaxm.ku_alarmy.data.crawl.CrawlRepository
import com.corgaxm.ku_alarmy.data.crawl.GraduationSimulationResponse
import com.corgaxm.ku_alarmy.data.db.GradeRepository
import com.corgaxm.ku_alarmy.data.db.GraduationSimulationData
import com.corgaxm.ku_alarmy.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val crawlRepository: CrawlRepository,
    private val gradeRepository: GradeRepository
) : ViewModel() {
    private val _graduationSimulationLoading = MutableLiveData(false)
    val graduationSimulationLoading get() = _graduationSimulationLoading

    var logoutResponse = MutableLiveData<Resource<Unit>>()

    var graduationSimulationResponse = MutableLiveData<Resource<GraduationSimulationResponse>>()

    var graduationSimulationData = MutableLiveData<Resource<List<GraduationSimulationData>>>()

    fun clearLogoutResource() {
        logoutResponse = MutableLiveData<Resource<Unit>>()
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
                graduationSimulationData.postValue(gradeRepository.getGraduationSimulations())
            }
            Log.d("yoonseop", "DB: ${graduationSimulationData.value}")
        }
    }

    fun fetchGraduationSimulationFromServer() {
        _graduationSimulationLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                graduationSimulationResponse.postValue(crawlRepository.makeGraduationSimulationRequest())
                graduationSimulationData.postValue(gradeRepository.getGraduationSimulations())
            }
//            Log.d("yoonseop", "DB: ${graduationSimulationData.value}")
            _graduationSimulationLoading.postValue(false)
        }
    }
}