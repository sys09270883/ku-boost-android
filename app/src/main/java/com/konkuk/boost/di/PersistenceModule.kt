package com.konkuk.boost.di

import androidx.room.Room
import com.konkuk.boost.persistence.AppDatabase
import com.konkuk.boost.persistence.AppMigration
import com.konkuk.boost.persistence.PreferenceManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val persistenceModule = module {
    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "boost-db")
            .addMigrations(AppMigration.MIGRATION_1_2)
            .addMigrations(AppMigration.MIGRATION_2_3)
            .build()
    }
    single { get<AppDatabase>().graduationSimulationDao() }
    single { get<AppDatabase>().gradeDao() }
    single { get<AppDatabase>().likeCourseDao() }
    single { get<AppDatabase>().rankDao() }
    single { PreferenceManager(androidApplication()) }
}