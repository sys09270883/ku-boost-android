package com.konkuk.boost.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilityModule = module {
    single { androidContext().assets }
    single { androidContext().filesDir }
}