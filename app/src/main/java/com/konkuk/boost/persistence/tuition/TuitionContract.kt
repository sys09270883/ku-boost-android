package com.konkuk.boost.persistence.tuition

import com.konkuk.boost.persistence.AppContract.AppEntry.ENTER_AMOUNT
import com.konkuk.boost.persistence.AppContract.AppEntry.PAID_DATE
import com.konkuk.boost.persistence.AppContract.AppEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.AppContract.AppEntry.SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.STATE_CODE
import com.konkuk.boost.persistence.AppContract.AppEntry.TUITION_AMOUNT
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.YEAR

object TuitionContract {
    const val TABLE_NAME = "tuition"

    const val CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $USERNAME TEXT NOT NULL DEFAULT null,
            $PAID_DATE TEXT NOT NULL DEFAULT null,
            $TUITION_AMOUNT INTEGER NOT NULL DEFAULT 0,
            $ENTER_AMOUNT INTEGER NOT NULL DEFAULT 0,
            $YEAR INTEGER NOT NULL DEFAULT null,
            $SEMESTER TEXT NOT NULL DEFAULT null,
            $STATE_CODE TEXT NOT NULL DEFAULT null,
            CONSTRAINT $PRIMARY_KEYS PRIMARY KEY (
            $USERNAME, $PAID_DATE)
        )
    """
}