package com.konkuk.boost.di

import com.konkuk.boost.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainFragmentViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { QRCodeViewModel(get(), get()) }
    viewModel { GradeViewModel(get(), get()) }
    viewModel { TotalGradeViewModel(get(), get()) }
    viewModel { GradeDetailViewModel() }
    viewModel { GraduationSimulationViewModel(get()) }
    viewModel { GraduationSimulationDetailViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { CertificateOfAcquiredViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }
    viewModel { InfoViewModel(get()) }
    viewModel { AcademicEventViewModel(get()) }
    viewModel { RegistrationAndScholarshipViewModel(get()) }
}