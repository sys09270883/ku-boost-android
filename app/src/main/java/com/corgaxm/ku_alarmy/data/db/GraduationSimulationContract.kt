package com.corgaxm.ku_alarmy.data.db

import android.provider.BaseColumns

object GraduationSimulationContract {
    object GraduationSimulationEntry : BaseColumns {
        const val TABLE_NAME = "graduation_simulation"
        const val USERNAME = "username"
        const val BASIC_ELECTIVE = "basicElective"
        const val GENERAL_ELECTIVE = "generalElective"
        const val CORE_ELECTIVE = "coreElective"
        const val NORMAL_ELECTIVE = "normalElective"
        const val GENERAL_REQUIREMENT = "generalRequirement"
        const val MAJOR_REQUIREMENT = "majorRequirement"
        const val MAJOR_ELECTIVE = "majorElective"
        const val DUAL_ELECTIVE = "dualElective"
        const val DUAL_REQUIREMENT = "dualRequirement"
        const val DUAL_MAJOR_ELECTIVE = "dualMajorElective"
        const val ETC = "etc"
        const val TYPE = "type"
        const val CREATED_AT = "createdAt"
        const val MODIFIED_AT = "modifiedAt"
    }

    private const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${GraduationSimulationEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${GraduationSimulationEntry.USERNAME} TEXT," +
                "${GraduationSimulationEntry.BASIC_ELECTIVE} INTEGER," +
                "${GraduationSimulationEntry.GENERAL_ELECTIVE} INTEGER," +
                "${GraduationSimulationEntry.CORE_ELECTIVE} INTEGER," +
                "${GraduationSimulationEntry.NORMAL_ELECTIVE} INTEGER," +
                "${GraduationSimulationEntry.GENERAL_REQUIREMENT} INTEGER," +
                "${GraduationSimulationEntry.MAJOR_REQUIREMENT} INTEGER," +
                "${GraduationSimulationEntry.MAJOR_ELECTIVE} INTEGER," +
                "${GraduationSimulationEntry.DUAL_ELECTIVE} INTEGER," +
                "${GraduationSimulationEntry.DUAL_REQUIREMENT} INTEGER," +
                "${GraduationSimulationEntry.DUAL_MAJOR_ELECTIVE} INTEGER," +
                "${GraduationSimulationEntry.ETC} INTEGER," +
                "${GraduationSimulationEntry.TYPE} TEXT)"

    private const val SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS ${GraduationSimulationEntry.TABLE_NAME}"
}