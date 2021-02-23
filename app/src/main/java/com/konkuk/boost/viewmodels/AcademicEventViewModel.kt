package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.persistence.dept.DeptTransferEntity
import com.konkuk.boost.persistence.stdstate.StudentStateChangeEntity
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.launch

class AcademicEventViewModel(
    val authRepository: AuthRepository
) : ViewModel() {

    val deptTransferResponse = MutableLiveData<UseCase<List<DeptTransferEntity>>>()

    fun fetchDeptTransferInfo() {
        viewModelScope.launch {
            deptTransferResponse.postValue(authRepository.getDeptTransferInfo())
        }
    }

    fun getDeptTransferInfo() =
        deptTransferResponse.value?.data?.sortedByDescending { it.changedDate } ?: emptyList()

    val studentStateChangeResponse = MutableLiveData<UseCase<List<StudentStateChangeEntity>>>()

    fun fetchStudentStateChangeInfo() {
        viewModelScope.launch {
            studentStateChangeResponse.postValue(authRepository.getStudentStateChangeInfo())
        }
    }

    fun getStudentStateChangeInfo() =
        studentStateChangeResponse.value?.data?.sortedByDescending { it.changedDate } ?: emptyList()
}