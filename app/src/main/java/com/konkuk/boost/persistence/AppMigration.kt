package com.konkuk.boost.persistence

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.konkuk.boost.persistence.area.SubjectAreaContract
import com.konkuk.boost.persistence.dept.DeptTransferContract
import com.konkuk.boost.persistence.grade.GradeContract
import com.konkuk.boost.persistence.like.LikeCourseContract
import com.konkuk.boost.persistence.personal.PersonalInfoContract
import com.konkuk.boost.persistence.rank.RankContract
import com.konkuk.boost.persistence.scholarship.ScholarshipContract
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract
import com.konkuk.boost.persistence.tuition.TuitionContract

object AppMigration {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // LikeCourse is deprecated.
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(RankContract.CREATE_TABLE)
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(GradeContract.ADD_COLUMN)
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(SubjectAreaContract.CREATE_TABLE)
        }
    }

    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(PersonalInfoContract.CREATE_TABLE)
        }
    }

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(DeptTransferContract.CREATE_TABLE)
        }
    }

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(StudentStateChangeContract.CREATE_TABLE)
        }
    }

    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(TuitionContract.CREATE_TABLE)
        }
    }

    val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(ScholarshipContract.CREATE_TABLE)
        }
    }

    val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(GradeContract.CREATE_NEW_TABLE_WITH_TYPE)
            database.execSQL(GradeContract.DROP_ORIGIN_TABLE)
            database.execSQL(GradeContract.RENAME_TMP_TO_GRADE_TABLE)
        }
    }

    val MIGRATION_11_12 = object : Migration(11, 12) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(LikeCourseContract.DROP_LIKE_COURSE_TABLE)
        }
    }
}