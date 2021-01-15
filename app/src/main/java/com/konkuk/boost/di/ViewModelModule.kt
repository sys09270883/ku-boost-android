package com.konkuk.boost.di

import com.konkuk.boost.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { TotalGradeViewModel(get()) }
    viewModel { GradeDetailViewModel() }
    viewModel { GraduationSimulationViewModel() }
}