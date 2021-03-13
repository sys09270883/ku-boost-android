package com.konkuk.boost.persistence.stdstate

import com.konkuk.boost.persistence.AppContract.AppEntry.APPLIED_STATE_CODE
import com.konkuk.boost.persistence.AppContract.AppEntry.APPLY_DATE
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_CODE
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_DATE
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_REASON
import com.konkuk.boost.persistence.AppContract.AppEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME

object StudentStateChangeContract {
    const val TABLE_NAME = "studentStateChange"

    const val CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $USERNAME TEXT NOT NULL DEFAULT null,
            $APPLY_DATE TEXT NOT NULL DEFAULT null,
            $CHANGED_DATE TEXT NOT NULL DEFAULT null,
            $CHANGED_CODE TEXT NOT NULL DEFAULT null,
            $CHANGED_REASON TEXT NOT NULL DEFAULT null,
            $APPLIED_STATE_CODE TEXT NOT NULL DEFAULT null,
            CONSTRAINT $PRIMARY_KEYS PRIMARY KEY (
            $USERNAME, $APPLY_DATE, $CHANGED_CODE)
        )
    """
}