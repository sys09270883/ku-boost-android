package com.corgaxm.ku_alarmy.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.corgaxm.ku_alarmy.data.db.GraduationSimulationContract.GraduationSimulationEntry
import java.util.*

@Entity(tableName = GraduationSimulationEntry.TABLE_NAME, primaryKeys = ["username", "type"])
data class GraduationSimulationData(
    @ColumnInfo(name = GraduationSimulationEntry.USERNAME) val username: String = "",
    @ColumnInfo(name = GraduationSimulationEntry.BASIC_ELECTIVE) val basicElective: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.ADVANCED_ELECTIVE) val advancedElective: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.GENERAL_ELECTIVE) val generalElective: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.CORE_ELECTIVE) val coreElective: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.NORMAL_ELECTIVE) val normalElective: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.GENERAL_REQUIREMENT) val generalRequirement: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.MAJOR_REQUIREMENT) val majorRequirement: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.MAJOR_ELECTIVE) val majorElective: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.DUAL_ELECTIVE) val dualElective: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.DUAL_REQUIREMENT) val dualRequirement: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.DUAL_MAJOR_ELECTIVE) val dualMajorElective: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.ETC) val etc: Int?,
    @ColumnInfo(name = GraduationSimulationEntry.TYPE) val type: String = "",
    @ColumnInfo(
        name = GraduationSimulationEntry.CREATED_AT,
        defaultValue = "CURRENT_TIMESTAMP"
    ) val createdAt: Long,
    @ColumnInfo(
        name = GraduationSimulationEntry.MODIFIED_AT,
        defaultValue = "CURRENT_TIMESTAMP"
    ) val modifiedAt: Long,
    @ColumnInfo(name = GraduationSimulationEntry.TOTAL) val total: Int?
) {

    override fun equals(other: Any?): Boolean {
        (other as? GraduationSimulationData)?.let {
            return (it.username == other.username) && (it.type == other.type)
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.username, this.type)
    }
}
