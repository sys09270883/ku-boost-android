package com.konkuk.boost.persistence

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.konkuk.boost.persistence.LikeCourseContract.LikeCourseEntry

object AppMigration {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val sql = "CREATE TABLE IF NOT EXISTS ${LikeCourseEntry.TABLE_NAME} (" +
                    "${LikeCourseEntry.USERNAME} TEXT NOT NULL DEFAULT null," +
                    "${LikeCourseEntry.YEAR} INTEGER NOT NULL DEFAULT null," +
                    "${LikeCourseEntry.SEMESTER} INTEGER NOT NULL DEFAULT null," +
                    "${LikeCourseEntry.SUBJECT_ID} TEXT NOT NULL DEFAULT null," +
                    "${LikeCourseEntry.LIKE} INTEGER NOT NULL DEFAULT null," +
                    "CONSTRAINT ${LikeCourseEntry.PRIMARY_KEYS} PRIMARY KEY (" +
                    "${LikeCourseEntry.USERNAME}, ${LikeCourseEntry.YEAR}, ${LikeCourseEntry.SEMESTER}, ${LikeCourseEntry.SUBJECT_ID})" +
                    ")"
            database.execSQL(sql)
        }
    }
}