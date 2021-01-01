package com.konkuk.boost.persistence

import android.provider.BaseColumns

object GraduationSimulationContract {
    object GraduationSimulationEntry : BaseColumns {
        const val TABLE_NAME = "graduation_simulation"
        const val USERNAME = "username"
        const val CLASSIFICATION = "classification"
        const val STANDARD = "standard"
        const val ACQUIRED = "acquired"
        const val REMAINDER = "remainder"
        const val MODIFIED_AT = "modified_at"
    }
}