package com.konkuk.boost.persistence.grade

import com.konkuk.boost.persistence.AppContract.AppEntry.CHARACTER_GRADE
import com.konkuk.boost.persistence.AppContract.AppEntry.CLASSIFICATION
import com.konkuk.boost.persistence.AppContract.AppEntry.EVALUATION_METHOD
import com.konkuk.boost.persistence.AppContract.AppEntry.GRADE
import com.konkuk.boost.persistence.AppContract.AppEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.AppContract.AppEntry.PROFESSOR
import com.konkuk.boost.persistence.AppContract.AppEntry.SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_AREA
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_ID
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_NAME
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_NUMBER
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_POINT
import com.konkuk.boost.persistence.AppContract.AppEntry.TYPE
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.YEAR

object GradeContract {
    const val TABLE_NAME = "grades"

    enum class Type(val value: Int) {
        VALID(0), PENDING(1), DELETED(2)
    }

    const val ADD_COLUMN = "ALTER TABLE $TABLE_NAME ADD COLUMN $SUBJECT_AREA TEXT NOT NULL DEFAULT null"

    const val CREATE_NEW_TABLE_WITH_TYPE = """
        CREATE TABLE TMP (
            $USERNAME TEXT NOT NULL DEFAULT null,
            $EVALUATION_METHOD TEXT NOT NULL DEFAULT null,
            $YEAR INTEGER NOT NULL DEFAULT null,
            $SEMESTER INTEGER NOT NULL DEFAULT null,
            $CLASSIFICATION TEXT NOT NULL DEFAULT null,
            $CHARACTER_GRADE TEXT NOT NULL DEFAULT null,
            $GRADE REAL NOT NULL DEFAULT null,
            $PROFESSOR TEXT NOT NULL DEFAULT null,
            $SUBJECT_ID TEXT NOT NULL DEFAULT null,
            $SUBJECT_NUMBER TEXT NOT NULL DEFAULT null,
            $SUBJECT_NAME TEXT NOT NULL DEFAULT null,
            $SUBJECT_POINT INTEGER NOT NULL DEFAULT null,
            $SUBJECT_AREA TEXT NOT NULL DEFAULT null,
            $TYPE INTEGER NOT NULL DEFAULT null,
            CONSTRAINT $PRIMARY_KEYS PRIMARY KEY ($USERNAME, $SUBJECT_ID, $SEMESTER, $YEAR)
        )
    """

    const val DROP_ORIGIN_TABLE = "DROP TABLE $TABLE_NAME"

    const val RENAME_TMP_TO_GRADE_TABLE = "ALTER TABLE TMP RENAME TO $TABLE_NAME"
}