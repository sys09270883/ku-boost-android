package com.konkuk.boost.persistence.personal

import com.konkuk.boost.persistence.AppContract.AppEntry.KEY
import com.konkuk.boost.persistence.AppContract.AppEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.VALUE

object PersonalInfoContract {
    const val TABLE_NAME = "personal_info"

    const val CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $USERNAME TEXT NOT NULL DEFAULT null,
            $KEY TEXT NOT NULL DEFAULT null,
            $VALUE TEXT NOT NULL DEFAULT null,
            CONSTRAINT $PRIMARY_KEYS PRIMARY KEY (
            $USERNAME, $KEY)
        )
    """
}