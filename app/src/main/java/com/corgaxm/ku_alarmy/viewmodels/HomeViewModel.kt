package com.corgaxm.ku_alarmy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corgaxm.ku_alarmy.data.auth.AuthRepository
import com.corgaxm.ku_alarmy.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val authRepository: AuthRepository): ViewModel() {

    var logoutResponse = MutableLiveData<Resource<Unit>>()

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                logoutResponse.postValue(authRepository.makeLogoutRequest())
            }
        }
    }

    fun clearLogoutResource() {
        logoutResponse = MutableLiveData<Resource<Unit>>()
    }
}