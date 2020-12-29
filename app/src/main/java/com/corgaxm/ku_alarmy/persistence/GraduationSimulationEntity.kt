package com.corgaxm.ku_alarmy.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationContract.GraduationSimulationEntry
import java.util.*

@Entity(
    tableName = GraduationSimulationEntry.TABLE_NAME,
    primaryKeys = ["username", "classification"]
)
data class GraduationSimulationEntity(
    @ColumnInfo(name = GraduationSimulationEntry.USERNAME) val username: String,
    @ColumnInfo(name = GraduationSimulationEntry.CLASSIFICATION) val classification: String,
    @ColumnInfo(name = GraduationSimulationEntry.STANDARD) val standard: Int,
    @ColumnInfo(name = GraduationSimulationEntry.ACQUIRED) val acquired: Int,
    @ColumnInfo(name = GraduationSimulationEntry.REMAINDER) val remainder: Int,
    @ColumnInfo(
        name = GraduationSimulationEntry.MODIFIED_AT,
        defaultValue = "CURRENT_TIMESTAMP"
    ) val modifiedAt: Long,
) {

    override fun equals(other: Any?): Boolean {
        (other as? GraduationSimulationEntity)?.let {
            return (it.username == other.username) && (it.classification == other.classification)
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.username, this.classification)
    }
}
