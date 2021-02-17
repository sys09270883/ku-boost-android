package com.konkuk.boost.persistence.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.konkuk.boost.persistence.area.SubjectAreaContract.SubjectAreaEntry
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry
import com.konkuk.boost.persistence.like.LikeCourseContract.LikeCourseEntry
import com.konkuk.boost.persistence.personal.PersonalInfoContract
import com.konkuk.boost.persistence.rank.RankContract.RankEntry

object AppMigration {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val sql = "CREATE TABLE IF NOT EXISTS ${LikeCourseEntry.TABLE_NAME} (" +
                    "${LikeCourseEntry.USERNAME} TEXT NOT NULL DEFAULT null," +
                    "${LikeCourseEntry.YEAR} INTEGER NOT NULL DEFAULT null," +
                    "${LikeCourseEntry.SEMESTER} INTEGER NOT NULL DEFAULT null," +
                    "${LikeCourseEntry.SUBJECT_ID} TEXT NOT NULL DEFAULT null," +
                    "${LikeCourseEntry.SUBJECT_NAME} TEXT NOT NULL DEFAULT null," +
                    "${LikeCourseEntry.PROFESSOR} TEXT NOT NULL DEFAULT null," +
                    "${LikeCourseEntry.LIKE} INTEGER NOT NULL DEFAULT null," +
                    "CONSTRAINT ${LikeCourseEntry.PRIMARY_KEYS} PRIMARY KEY (" +
                    "${LikeCourseEntry.USERNAME}, ${LikeCourseEntry.YEAR}, ${LikeCourseEntry.SEMESTER}, ${LikeCourseEntry.SUBJECT_ID})" +
                    ")"
            database.execSQL(sql)
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val sql = "CREATE TABLE IF NOT EXISTS ${RankEntry.TABLE_NAME} (" +
                    "${RankEntry.USERNAME} TEXT NOT NULL DEFAULT null," +
                    "${RankEntry.YEAR} INTEGER NOT NULL DEFAULT null," +
                    "${RankEntry.SEMESTER} INTEGER NOT NULL DEFAULT null," +
                    "${RankEntry.RANK} INTEGER NOT NULL DEFAULT null," +
                    "${RankEntry.TOTAL} INTEGER NOT NULL DEFAULT null," +
                    "CONSTRAINT ${RankEntry.PRIMARY_KEYS} PRIMARY KEY (" +
                    "${RankEntry.USERNAME}, ${RankEntry.YEAR}, ${RankEntry.SEMESTER})" +
                    ")"
            database.execSQL(sql)
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val sql = "ALTER TABLE ${GradeEntry.TABLE_NAME} " +
                    "ADD COLUMN ${GradeEntry.SUBJECT_AREA} TEXT NOT NULL DEFAULT \'\'"
            database.execSQL(sql)
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val sql = "CREATE TABLE IF NOT EXISTS ${SubjectAreaEntry.TABLE_NAME} (" +
                    "${SubjectAreaEntry.USERNAME} TEXT NOT NULL DEFAULT null," +
                    "${SubjectAreaEntry.TYPE} INTEGER NOT NULL DEFAULT null," +
                    "${SubjectAreaEntry.SUBJECT_AREA_NAME} TEXT NOT NULL DEFAULT null," +
                    "CONSTRAINT ${SubjectAreaEntry.PRIMARY_KEYS} PRIMARY KEY (" +
                    "${SubjectAreaEntry.USERNAME}, ${SubjectAreaEntry.SUBJECT_AREA_NAME})" +
                    ")"
            database.execSQL(sql)
        }
    }

    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(PersonalInfoContract.CREATE_SQL)
        }
    }
}