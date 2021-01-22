package com.konkuk.boost.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GraduationSimulationEntity::class, GradeEntity::class, LikeCourseEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun graduationSimulationDao(): GraduationSimulationDao
    abstract fun gradeDao(): GradeDao
    abstract fun likeCourseDao(): LikeCourseDao
}