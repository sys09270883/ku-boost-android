package com.konkuk.boost.persistence.scholarship

import com.konkuk.boost.persistence.AppContract.AppEntry.SCHOLARSHIP_DATE
import com.konkuk.boost.persistence.AppContract.AppEntry.ETC_AMOUNT
import com.konkuk.boost.persistence.AppContract.AppEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.AppContract.AppEntry.SCHOLARSHIP_ENTER_AMOUNT
import com.konkuk.boost.persistence.AppContract.AppEntry.SCHOLARSHIP_NAME
import com.konkuk.boost.persistence.AppContract.AppEntry.SCHOLARSHIP_TUITION_AMOUNT
import com.konkuk.boost.persistence.AppContract.AppEntry.SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.YEAR

object ScholarshipContract {
    const val TABLE_NAME = "scholarships"

    const val CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $USERNAME TEXT NOT NULL DEFAULT null,
            $SCHOLARSHIP_NAME TEXT NOT NULL DEFAULT null,
            $SCHOLARSHIP_ENTER_AMOUNT INTEGER NOT NULL DEFAULT 0,
            $SCHOLARSHIP_TUITION_AMOUNT INTEGER NOT NULL DEFAULT 0,
            $ETC_AMOUNT INTEGER NOT NULL DEFAULT 0,
            $YEAR TEXT NOT NULL DEFAULT null,
            $SEMESTER TEXT NOT NULL DEFAULT null,
            $SCHOLARSHIP_DATE TEXT NOT NULL DEFAULT null,
            CONSTRAINT $PRIMARY_KEYS PRIMARY KEY (
            $USERNAME, $SCHOLARSHIP_NAME)
        )
    """
}