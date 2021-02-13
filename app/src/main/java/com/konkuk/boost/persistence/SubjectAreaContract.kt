package com.konkuk.boost.persistence

import android.provider.BaseColumns

object SubjectAreaContract {
    object SubjectAreaEntry : BaseColumns {
        const val TABLE_NAME = "subject_area"
        const val USERNAME = "username"
        const val TYPE = "type"
        const val SUBJECT_AREA_NAME = "subjectAreaName"
        const val PRIMARY_KEYS = "primaryKeys"
    }
}