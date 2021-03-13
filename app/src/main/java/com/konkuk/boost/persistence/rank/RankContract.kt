package com.konkuk.boost.persistence.rank

import com.konkuk.boost.persistence.AppContract.AppEntry.PRIMARY_KEYS
import com.konkuk.boost.persistence.AppContract.AppEntry.RANK
import com.konkuk.boost.persistence.AppContract.AppEntry.SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.TOTAL
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.YEAR

object RankContract {
    const val TABLE_NAME = "ranks"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "$USERNAME TEXT NOT NULL DEFAULT null," +
            "$YEAR INTEGER NOT NULL DEFAULT null," +
            "$SEMESTER INTEGER NOT NULL DEFAULT null," +
            "$RANK INTEGER NOT NULL DEFAULT null," +
            "$TOTAL INTEGER NOT NULL DEFAULT null," +
            "CONSTRAINT $PRIMARY_KEYS PRIMARY KEY (" +
            "${USERNAME}, ${YEAR}, ${SEMESTER}))"
}