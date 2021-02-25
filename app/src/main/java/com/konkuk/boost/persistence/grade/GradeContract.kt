package com.konkuk.boost.persistence.grade

import android.provider.BaseColumns
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.CHARACTER_GRADE
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.CLASSIFICATION
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.EVALUATION_METHOD
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.GRADE
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.PROFESSOR
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.SEMESTER
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.SUBJECT_AREA
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.SUBJECT_ID
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.SUBJECT_NAME
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.SUBJECT_NUMBER
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.SUBJECT_POINT
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.TABLE_NAME
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.TYPE
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.USERNAME
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.YEAR

object GradeContract {
    object GradeEntry : BaseColumns {
        const val TABLE_NAME = "grades"
        const val USERNAME = "username"
        const val EVALUATION_METHOD = "evaluationMethod"
        const val YEAR = "year"
        const val SEMESTER = "semester"
        const val CLASSIFICATION = "classification"
        const val CHARACTER_GRADE = "characterGrade"
        const val GRADE = "grade"
        const val PROFESSOR = "professor"
        const val SUBJECT_ID = "subjectId"
        const val SUBJECT_NUMBER = "subjectNumber"
        const val SUBJECT_NAME = "subjectName"
        const val SUBJECT_POINT = "subjectPoint"
        const val SUBJECT_AREA = "subjectArea"
        const val TYPE = "type"
        const val PRIMARY_KEYS = "primary_keys"
    }

    enum class Type(val value: Int) {
        VALID(0), PENDING(1), DELETED(2)
    }

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