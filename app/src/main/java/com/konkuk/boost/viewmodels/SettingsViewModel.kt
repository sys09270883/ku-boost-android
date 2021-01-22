package com.konkuk.boost.viewmodels

import androidx.lifecycle.ViewModel
import com.konkuk.boost.repositories.AuthRepository

class SettingsViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

}