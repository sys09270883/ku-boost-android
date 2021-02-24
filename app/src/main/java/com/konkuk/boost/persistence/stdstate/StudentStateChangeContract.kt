package com.konkuk.boost.persistence.stdstate

import android.provider.BaseColumns
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry.APPLIED_STATE_CODE
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry.APPLY_DATE
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry.CHANGED_CODE
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry.CHANGED_DATE
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry.CHANGED_REASON
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry.TABLE_NAME
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry.USERNAME

object StudentStateChangeContract {
    object StudentStateChangeEntry : BaseColumns {
        const val TABLE_NAME = "studentStateChange"
        const val USERNAME = "username"
        const val APPLY_DATE = "applyDate"
        const val CHANGED_DATE = "changedDate"
        const val CHANGED_CODE = "changedCode"
        const val CHANGED_REASON = "changedReason"
        const val APPLIED_STATE_CODE = "appliedStateCode"
        const val PRIMARY_KEYS = "primary_keys"
    }

    const val CREATE_SQL = """
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