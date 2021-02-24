package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.persistence.scholarship.ScholarshipEntity
import com.konkuk.boost.persistence.tuition.TuitionEntity
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.launch

class RegistrationAndScholarshipViewModel(
    val authRepository: AuthRepository
) : ViewModel() {
    val tuitionResponse = MutableLiveData<UseCase<List<TuitionEntity>>>()

    fun fetchTuitionResponse() {
        viewModelScope.launch {
            tuitionResponse.postValue(authRepository.getTuitionInfo())
        }
    }

    fun getTuition() =
        tuitionResponse.value?.data?.sortedByDescending { it.paidDate } ?: emptyList()

    val scholarshipResponse = MutableLiveData<UseCase<List<ScholarshipEntity>>>()

    fun fetchScholarshipResponse() {
        viewModelScope.launch {
            scholarshipResponse.postValue(authRepository.getScholarshipInfo())
        }
    }

    fun getScholarship() =
        scholarshipResponse.value?.data?.sortedByDescending { it.date } ?: emptyList()
}