package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.library.QRResponse
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.repositories.LibraryRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QRCodeViewModel(
    private val libraryRepository: LibraryRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val qrResponse = MutableLiveData<UseCase<QRResponse>>()

    fun getMobileQRCode() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                qrResponse.postValue(libraryRepository.makeMobileQRCodeRequest())
            }
        }
    }

    val name = MutableLiveData(authRepository.getName())

    val dept = MutableLiveData(authRepository.getDept())

    val stdNo = MutableLiveData(authRepository.getStdNo())

}