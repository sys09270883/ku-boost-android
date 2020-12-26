package com.corgaxm.ku_alarmy.di

import com.corgaxm.ku_alarmy.utils.SettingsManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilityModule = module {
    single { SettingsManager(androidContext()) }
}