package com.corgaxm.ku_alarmy.persistence

import android.provider.BaseColumns

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
        const val VALID = "valid"
        const val MODIFIED_AT = "modifiedAt"
    }
}