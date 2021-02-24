package com.konkuk.boost.persistence.tuition

import android.provider.BaseColumns
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.ENTER_AMOUNT
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.PAID_DATE
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.SEMESTER
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.STATE_CODE
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.TABLE_NAME
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.TUITION_AMOUNT
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.USERNAME
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.YEAR

object TuitionContract {
    object TuitionEntry : BaseColumns {
        const val TABLE_NAME = "tuition"
        const val USERNAME = "username"
        const val PAID_DATE = "paidDate"
        const val TUITION_AMOUNT = "tuitionAmount"
        const val ENTER_AMOUNT = "enterAmount"
        const val YEAR = "year"
        const val SEMESTER = "semester"
        const val STATE_CODE = "stateCode"
        const val PRIMARY_KEYS = "primary_keys"
    }

    const val CREATE_SQL = """
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