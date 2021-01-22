package com.konkuk.boost.persistence

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.konkuk.boost.persistence.LikeCourseContract.LikeCourseEntry

object AppMigration {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val sql = "CREATE TABLE IF NOT EXISTS ${LikeCourseEntry.TABLE_NAME} (" +
                    "${LikeCourseEntry.USERNAME} TEXT NOT NULL DEFAULT ''," +
                    "${LikeCourseEntry.YEAR} INTEGER NOT NULL DEFAULT 0," +
                    "${LikeCourseEntry.SEMESTER} INTEGER NOT NULL DEFAULT 0," +
                    "${LikeCourseEntry.SUBJECT_ID} TEXT NOT NULL DEFAULT ''," +
                    "${LikeCourseEntry.LIKE} INTEGER NOT NULL DEFAULT 0)"
            database.execSQL(sql)
        }
    }
}