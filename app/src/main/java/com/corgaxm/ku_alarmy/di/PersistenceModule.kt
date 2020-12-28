package com.corgaxm.ku_alarmy.di

import androidx.room.Room
import com.corgaxm.ku_alarmy.data.db.AppDatabase
import com.corgaxm.ku_alarmy.utils.SettingsManager
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val persistenceModule = module {
    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "alarmy-db").build()
    }
    single { get<AppDatabase>().graduationSimulationDao() }
    single { SettingsManager(androidContext()) }
}