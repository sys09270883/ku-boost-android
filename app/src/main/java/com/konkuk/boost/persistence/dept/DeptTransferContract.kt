package com.konkuk.boost.persistence.dept

import android.provider.BaseColumns

object DeptTransferContract {
    object DeptTransferEntry : BaseColumns {
        const val TABLE_NAME = "deptTransferInfo"
        const val USERNAME = "username"
        const val BEFORE_DEPT = "beforeDept"
        const val BEFORE_SUST = "beforeSust"
        const val BEFORE_MAJOR = "beforeMajor"
        const val CHANGED_CODE = "changedCode"
        const val CHANGED_DATE = "changedDate"
        const val CHANGED_YEAR = "changedYear"
        const val CHANGED_SEMESTER = "changedSemester"
        const val DEPT = "dept"
        const val SUST = "sust"
        const val MAJOR = "major"
        const val PRIMARY_KEYS = "primary_keys"
    }

    const val CREATE_SQL = """
        CREATE TABLE IF NOT EXISTS ${DeptTransferEntry.TABLE_NAME} (
            ${DeptTransferEntry.USERNAME} TEXT NOT NULL DEFAULT null,
            ${DeptTransferEntry.BEFORE_DEPT} TEXT NOT NULL DEFAULT null,
            ${DeptTransferEntry.BEFORE_SUST} TEXT NOT NULL DEFAULT null,
            ${DeptTransferEntry.BEFORE_MAJOR} TEXT NOT NULL DEFAULT null,
            ${DeptTransferEntry.CHANGED_CODE} TEXT NOT NULL DEFAULT null,
            ${DeptTransferEntry.CHANGED_DATE} TEXT NOT NULL DEFAULT null,
            ${DeptTransferEntry.CHANGED_YEAR} TEXT NOT NULL DEFAULT null,
            ${DeptTransferEntry.CHANGED_SEMESTER} TEXT NOT NULL DEFAULT null,
            ${DeptTransferEntry.DEPT} TEXT NOT NULL DEFAULT null,
            ${DeptTransferEntry.SUST} TEXT NOT NULL DEFAULT null,
            ${DeptTransferEntry.MAJOR} TEXT NOT NULL DEFAULT null,
            CONSTRAINT ${DeptTransferEntry.PRIMARY_KEYS} PRIMARY KEY (
            ${DeptTransferEntry.USERNAME}, 
            ${DeptTransferEntry.CHANGED_CODE},
            ${DeptTransferEntry.CHANGED_DATE})
        )
    """
}