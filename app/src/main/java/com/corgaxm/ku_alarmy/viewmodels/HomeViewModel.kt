package com.corgaxm.ku_alarmy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corgaxm.ku_alarmy.data.auth.AuthRepository
import com.corgaxm.ku_alarmy.data.crawl.CrawlRepository
import com.corgaxm.ku_alarmy.data.crawl.GraduationSimulationResponse
import com.corgaxm.ku_alarmy.data.db.GradeRepository
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

    fun fetchGraduationSimulation() {
        _graduationSimulationLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                graduationSimulationResponse.postValue(crawlRepository.makeGraduationSimulationRequest())
            }
            _graduationSimulationLoading.postValue(false)
        }
    }
}