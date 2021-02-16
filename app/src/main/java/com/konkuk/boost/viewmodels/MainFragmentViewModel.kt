package com.konkuk.boost.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.library.LoginResponse
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.repositories.GradeRepository
import com.konkuk.boost.repositories.LibraryRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragmentViewModel(
    private val authRepository: AuthRepository,
    private val gradeRepository: GradeRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {
    private val _allGradesLoading = MutableLiveData(false)
    val allGradesLoading get() = _allGradesLoading

    fun hasData(): Boolean = gradeRepository.hasData()

    val fetched = MutableLiveData(false)

    fun isNotFetched() = fetched.value != true

    val libraryLoginResponse = MutableLiveData<UseCase<LoginResponse>>()

    fun libraryLogin() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                libraryLoginResponse.postValue(libraryRepository.makeLoginRequest())
            }
        }
    }

    fun fetchStudentInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                authRepository.makeStudentInfoRequest()
            }
            Log.d("ku-boost", "Student information is fetched.")
        }
    }

    fun fetchValidGradesAndUpdateClassificationFromServer() {
        _allGradesLoading.value = true

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gradeRepository.makeValidGradesAndSimulation()
            }
            Log.d(
                "ku-boost",
                "All valid grades, classification of graduation simulation are updated."
            )
            _allGradesLoading.postValue(false)
            fetched.postValue(true)
        }
    }

    fun fetchGraduationSimulationFromServer() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gradeRepository.makeGraduationSimulationRequest()
            }
        }
    }

    fun makeTotalRank() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gradeRepository.makeTotalRank()
            }
            Log.d("ku-boost", "Total rank is generated.")
        }
    }
}