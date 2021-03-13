package com.konkuk.boost.persistence.area

import com.konkuk.boost.persistence.AppContract.AppEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_AREA_NAME
import com.konkuk.boost.persistence.AppContract.AppEntry.TYPE
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME

object SubjectAreaContract {
    const val TABLE_NAME = "subject_area"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "$USERNAME TEXT NOT NULL DEFAULT null," +
            "$TYPE INTEGER NOT NULL DEFAULT null," +
            "$SUBJECT_AREA_NAME TEXT NOT NULL DEFAULT null," +
            "CONSTRAINT $PRIMARY_KEYS PRIMARY KEY (" +
            "${USERNAME}, ${SUBJECT_AREA_NAME})" +
            ")"
}