package com.konkuk.boost.viewmodels

import androidx.lifecycle.ViewModel
import com.konkuk.boost.repositories.AuthRepository

class CertificateOfAcquiredViewModel(private val authRepository: AuthRepository) : ViewModel() {

    companion object {
        const val FILE = "certificate_of_acquired.html"
    }

    fun getStdNo() = authRepository.getStdNo()

    fun getFile() = FILE
}