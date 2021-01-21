package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.library.LoginResponse
import com.konkuk.boost.repositories.LibraryRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragmentViewModel(
    private val libraryRepository: LibraryRepository
): ViewModel() {

    val loginResponse = MutableLiveData<UseCase<LoginResponse>>()

    fun login() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loginResponse.postValue(libraryRepository.makeLoginRequest())
            }
        }
    }
}