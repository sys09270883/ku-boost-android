package com.konkuk.boost.persistence.scholarship

import android.provider.BaseColumns
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.DATE
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.ETC_AMOUNT
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.SCHOLARSHIP_ENTER_AMOUNT
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.SCHOLARSHIP_NAME
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.SCHOLARSHIP_TUITION_AMOUNT
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.SEMESTER
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.TABLE_NAME
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.USERNAME
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.YEAR

object ScholarshipContract {
    object ScholarshipEntry : BaseColumns {
        const val TABLE_NAME = "scholarships"
        const val USERNAME = "username"
        const val SCHOLARSHIP_NAME = "scholarshipName"
        const val SCHOLARSHIP_ENTER_AMOUNT = "scholarshipEnterAmount"
        const val SCHOLARSHIP_TUITION_AMOUNT = "scholarshipTuitionAmount"
        const val ETC_AMOUNT = "etcAmount"
        const val YEAR = "year"
        const val SEMESTER = "semester"
        const val DATE = "date"
        const val PRIMARY_KEYS = "primary_keys"
    }

    const val CREATE_SQL = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $USERNAME TEXT NOT NULL DEFAULT null,
            $SCHOLARSHIP_NAME TEXT NOT NULL DEFAULT null,
            $SCHOLARSHIP_ENTER_AMOUNT INTEGER NOT NULL DEFAULT 0,
            $SCHOLARSHIP_TUITION_AMOUNT INTEGER NOT NULL DEFAULT 0,
            $ETC_AMOUNT INTEGER NOT NULL DEFAULT 0,
            $YEAR TEXT NOT NULL DEFAULT null,
            $SEMESTER TEXT NOT NULL DEFAULT null,
            $DATE TEXT NOT NULL DEFAULT null,
            CONSTRAINT $PRIMARY_KEYS PRIMARY KEY (
            $USERNAME, $SCHOLARSHIP_NAME)
        )
    """
}