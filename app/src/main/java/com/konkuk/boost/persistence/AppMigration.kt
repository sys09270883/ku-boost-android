package com.konkuk.boost.persistence

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.konkuk.boost.persistence.area.SubjectAreaContract.SubjectAreaEntry
import com.konkuk.boost.persistence.dept.DeptTransferContract
import com.konkuk.boost.persistence.grade.GradeContract
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry
import com.konkuk.boost.persistence.like.LikeCourseContract.LikeCourseEntry
import com.konkuk.boost.persistence.personal.PersonalInfoContract
import com.konkuk.boost.persistence.rank.RankContract.RankEntry
import com.konkuk.boost.persistence.scholarship.ScholarshipContract
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract
import com.konkuk.boost.persistence.tuition.TuitionContract

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

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(DeptTransferContract.CREATE_SQL)
        }
    }

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(StudentStateChangeContract.CREATE_SQL)
        }
    }

    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(TuitionContract.CREATE_SQL)
        }
    }

    val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(ScholarshipContract.CREATE_SQL)
        }
    }

    val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(GradeContract.CREATE_NEW_TABLE_WITH_TYPE)
            database.execSQL(GradeContract.DROP_ORIGIN_TABLE)
            database.execSQL(GradeContract.RENAME_TMP_TO_GRADE_TABLE)
        }
    }

}