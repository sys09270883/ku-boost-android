package com.corgaxm.ku_alarmy.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GraduationSimulationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun graduationSimulationDao(): GraduationSimulationDao
}