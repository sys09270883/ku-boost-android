package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.konkuk.boost.repositories.AuthRepository

class InfoViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    val stdNo = MutableLiveData(authRepository.getStdNo())
}