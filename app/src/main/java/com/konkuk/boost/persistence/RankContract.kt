package com.konkuk.boost.persistence

import android.provider.BaseColumns

object RankContract {
    object RankEntry: BaseColumns {
        const val TABLE_NAME = "ranks"
        const val USERNAME = "username"
        const val YEAR = "year"
        const val SEMESTER = "semester"
        const val RANK = "rank"
        const val TOTAL = "total"
        const val PRIMARY_KEYS = "primary_keys"
    }
}