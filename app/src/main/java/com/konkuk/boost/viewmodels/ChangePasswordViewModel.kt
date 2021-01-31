package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.auth.ChangePasswordResponse
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordViewModel(private val authRepository: AuthRepository) : ViewModel() {

    companion object {
        val PASSWORD_REGEX =
            Regex("^.*(?=^.{8,20}\$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~`!@#\$%^&+=\\-()]).*\$")
    }

    val isOk: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().apply {
            postValue(0)
        }
    }

    val username = MutableLiveData("")

    fun setUsername(id: String) {
        username.value = id
    }

    val beforePassword = MutableLiveData("")

    fun setBeforePassword(pwd: String) {
        beforePassword.value = pwd
    }

    val password = MutableLiveData("")

    val password2 = MutableLiveData("")

    val isPasswordValid = MutableLiveData(false)

    val isPasswordSame = MutableLiveData(false)

    fun isFirstPasswordEmpty() = password.value == ""

    fun isSecondPasswordEmpty() = password2.value == ""

    fun checkPasswordValid(pwd: String) {
        val state = isOk.value ?: 0
        if (pwd.matches(PASSWORD_REGEX)) {
            isOk.postValue(0b01)
            isPasswordValid.postValue(true)
        } else {
            isOk.postValue(state and ((1 shl 0).inv()))
            isPasswordValid.postValue(false)
        }
    }

    fun updatePasswordState(password: String, password2: String) {
        if (isPasswordValid.value != true) {
            return
        }

        val state = isOk.value ?: 0
        if (password == password2) {
            isPasswordSame.postValue(true)
            isOk.postValue(state or 0b10)
        } else {
            isPasswordSame.postValue(false)
            isOk.postValue(state and ((1 shl 1).inv()))
        }
    }

    private val _loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            postValue(false)
        }
    }
    val loading get() = _loading

    val changePasswordResponse = MutableLiveData<UseCase<ChangePasswordResponse>>()

    fun changePassword() {
        val username = username.value ?: return
        val beforePassword = beforePassword.value ?: return
        val password = password.value ?: return
        val password2 = password2.value ?: return
        _loading.value = true

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                changePasswordResponse.postValue(
                    authRepository.makeChangePasswordRequest(
                        username,
                        beforePassword,
                        password,
                        password2
                    )
                )
            }
            _loading.postValue(false)
        }
    }
}