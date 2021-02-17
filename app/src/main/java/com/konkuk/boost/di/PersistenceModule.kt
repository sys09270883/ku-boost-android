package com.konkuk.boost.di

import androidx.room.Room
import com.konkuk.boost.persistence.room.AppDatabase
import com.konkuk.boost.persistence.room.AppMigration
import com.konkuk.boost.persistence.pref.PreferenceManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val persistenceModule = module {
    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "boost-db")
            .addMigrations(AppMigration.MIGRATION_1_2)
            .addMigrations(AppMigration.MIGRATION_2_3)
            .addMigrations(AppMigration.MIGRATION_3_4)
            .addMigrations(AppMigration.MIGRATION_4_5)
            .build()
    }
    single { get<AppDatabase>().graduationSimulationDao() }
    single { get<AppDatabase>().gradeDao() }
    single { get<AppDatabase>().likeCourseDao() }
    single { get<AppDatabase>().rankDao() }
    single { get<AppDatabase>().subjectAreaDao() }
    single { PreferenceManager(androidApplication()) }
}