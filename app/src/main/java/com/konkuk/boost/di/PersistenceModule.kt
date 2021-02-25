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
            .addMigrations(AppMigration.MIGRATION_3_4)
            .addMigrations(AppMigration.MIGRATION_4_5)
            .addMigrations(AppMigration.MIGRATION_5_6)
            .addMigrations(AppMigration.MIGRATION_6_7)
            .addMigrations(AppMigration.MIGRATION_7_8)
            .addMigrations(AppMigration.MIGRATION_8_9)
            .addMigrations(AppMigration.MIGRATION_9_10)
            .addMigrations(AppMigration.MIGRATION_10_11)
            .build()
    }
    single { get<AppDatabase>().graduationSimulationDao() }
    single { get<AppDatabase>().gradeDao() }
    single { get<AppDatabase>().likeCourseDao() }
    single { get<AppDatabase>().rankDao() }
    single { get<AppDatabase>().subjectAreaDao() }
    single { get<AppDatabase>().personalInfoDao() }
    single { get<AppDatabase>().deptTransferDao() }
    single { get<AppDatabase>().studentStateChangeDao() }
    single { get<AppDatabase>().tuitionDao() }
    single { get<AppDatabase>().scholarshipDao() }
    single { PreferenceManager(androidApplication()) }
}