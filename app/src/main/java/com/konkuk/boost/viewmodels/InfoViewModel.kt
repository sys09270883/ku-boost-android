package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.persistence.personal.PersonalInfoEntity
import com.konkuk.boost.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InfoViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val stdNo = MutableLiveData(authRepository.getStdNo())

    val name = MutableLiveData(authRepository.getName())

    val dept = MutableLiveData(authRepository.getDept())

    val state = MutableLiveData(authRepository.getState())

    val personalInfoResponse = MutableLiveData<List<PersonalInfoEntity>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            personalInfoResponse.postValue(
                authRepository.getPersonalInfo().data ?: emptyList()
            )
        }
    }

}