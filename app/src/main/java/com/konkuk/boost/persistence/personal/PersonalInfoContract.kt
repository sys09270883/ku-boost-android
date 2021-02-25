package com.konkuk.boost.persistence.personal

import android.provider.BaseColumns
import com.konkuk.boost.persistence.personal.PersonalInfoContract.PersonalInfoEntry.KEY
import com.konkuk.boost.persistence.personal.PersonalInfoContract.PersonalInfoEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.personal.PersonalInfoContract.PersonalInfoEntry.TABLE_NAME
import com.konkuk.boost.persistence.personal.PersonalInfoContract.PersonalInfoEntry.USERNAME
import com.konkuk.boost.persistence.personal.PersonalInfoContract.PersonalInfoEntry.VALUE

object PersonalInfoContract {
    object PersonalInfoEntry : BaseColumns {
        const val TABLE_NAME = "personal_info"
        const val USERNAME = "username"
        const val KEY = "key"
        const val VALUE = "value"
        const val PRIMARY_KEYS = "primary_keys"
    }

    const val CREATE_SQL = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $USERNAME TEXT NOT NULL DEFAULT null,
            $KEY TEXT NOT NULL DEFAULT null,
            $VALUE TEXT NOT NULL DEFAULT null,
            CONSTRAINT $PRIMARY_KEYS PRIMARY KEY (
            $USERNAME, $KEY)
        )
    """
}