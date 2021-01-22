package com.konkuk.boost.persistence

import android.provider.BaseColumns

object LikeCourseContract {
    object LikeCourseEntry : BaseColumns {
        const val TABLE_NAME = "like_courses"
        const val USERNAME = "username"
        const val YEAR = "year"
        const val SEMESTER = "semester"
        const val SUBJECT_ID = "subject_id"
        const val SUBJECT_NAME = "subject_name"
        const val PROFESSOR = "professor"
        const val LIKE = "_like"
        const val PRIMARY_KEYS = "primary_keys"
    }
}