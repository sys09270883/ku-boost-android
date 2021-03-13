package com.konkuk.boost.persistence.simul

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.AppContract.AppEntry.ACQUIRED
import com.konkuk.boost.persistence.AppContract.AppEntry.CLASSIFICATION
import com.konkuk.boost.persistence.AppContract.AppEntry.MODIFIED_AT
import com.konkuk.boost.persistence.AppContract.AppEntry.REMAINDER
import com.konkuk.boost.persistence.AppContract.AppEntry.STANDARD
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import java.util.*

@Entity(
    tableName = GraduationSimulationContract.TABLE_NAME,
    primaryKeys = [USERNAME, CLASSIFICATION]
)
data class GraduationSimulationEntity(
    @ColumnInfo(name = USERNAME) val username: String,
    @ColumnInfo(name = CLASSIFICATION) val classification: String,
    @ColumnInfo(name = STANDARD) val standard: Int,
    @ColumnInfo(name = ACQUIRED) val acquired: Int,
    @ColumnInfo(name = REMAINDER) val remainder: Int,
    @ColumnInfo(
        name = MODIFIED_AT,
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
