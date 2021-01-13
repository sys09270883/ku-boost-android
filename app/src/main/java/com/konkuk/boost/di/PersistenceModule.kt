package com.konkuk.boost.di

import androidx.room.Room
import com.konkuk.boost.persistence.AppDatabase
import com.konkuk.boost.persistence.PreferenceManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val persistenceModule = module {
    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "boost-db").build()
    }
    single { get<AppDatabase>().graduationSimulationDao() }
    single { get<AppDatabase>().gradeDao() }
    single { PreferenceManager(androidApplication()) }
}