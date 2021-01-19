package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.konkuk.boost.repositories.CourseRepository

class CourseViewModel(
    private val courseRepository: CourseRepository,
) : ViewModel() {

    val isFabOpened = MutableLiveData(false)

    fun setFabOpened(isOpened: Boolean) {
        isFabOpened.value = isOpened
    }

    fun isFabOpened() = isFabOpened.value ?: false

}