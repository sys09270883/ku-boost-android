package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.library.QRResponse
import com.konkuk.boost.repositories.LibraryRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QRCodeViewModel(
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    val qrResponse = MutableLiveData<UseCase<QRResponse>>()
    
    fun getMobileQRCode() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                qrResponse.postValue(libraryRepository.makeMobileQRCodeRequest())
            }
        }
    }

}