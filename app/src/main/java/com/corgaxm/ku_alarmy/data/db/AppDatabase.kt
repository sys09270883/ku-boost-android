package com.corgaxm.ku_alarmy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GraduationSimulationData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun graduationSimulationDao(): GraduationSimulationDao
}