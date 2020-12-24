package com.corgaxm.ku_alarmy.di

import com.corgaxm.ku_alarmy.viewmodels.HomeViewModel
import com.corgaxm.ku_alarmy.viewmodels.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel() }
}