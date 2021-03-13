package com.konkuk.boost.persistence.dept

import com.konkuk.boost.persistence.AppContract.AppEntry.BEFORE_DEPT
import com.konkuk.boost.persistence.AppContract.AppEntry.BEFORE_MAJOR
import com.konkuk.boost.persistence.AppContract.AppEntry.BEFORE_SUST
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_CODE
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_DATE
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_YEAR
import com.konkuk.boost.persistence.AppContract.AppEntry.DEPT
import com.konkuk.boost.persistence.AppContract.AppEntry.MAJOR
import com.konkuk.boost.persistence.AppContract.AppEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.AppContract.AppEntry.SUST
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME

object DeptTransferContract {
    const val TABLE_NAME = "deptTransferInfo"

    const val CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $USERNAME TEXT NOT NULL DEFAULT null,
            $BEFORE_DEPT TEXT NOT NULL DEFAULT null,
            $BEFORE_SUST TEXT NOT NULL DEFAULT null,
            $BEFORE_MAJOR TEXT NOT NULL DEFAULT null,
            $CHANGED_CODE TEXT NOT NULL DEFAULT null,
            $CHANGED_DATE TEXT NOT NULL DEFAULT null,
            $CHANGED_YEAR TEXT NOT NULL DEFAULT null,
            $CHANGED_SEMESTER TEXT NOT NULL DEFAULT null,
            $DEPT TEXT NOT NULL DEFAULT null,
            $SUST TEXT NOT NULL DEFAULT null,
            $MAJOR TEXT NOT NULL DEFAULT null,
            CONSTRAINT $PRIMARY_KEYS PRIMARY KEY (
            ${USERNAME}, 
            ${CHANGED_CODE},
            ${CHANGED_DATE})
        )
    """
}