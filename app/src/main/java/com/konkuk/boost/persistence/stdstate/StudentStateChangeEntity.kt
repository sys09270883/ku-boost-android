package com.konkuk.boost.persistence.stdstate

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry
import java.util.*

@Entity(
    tableName = StudentStateChangeEntry.TABLE_NAME,
    primaryKeys = [
        StudentStateChangeEntry.USERNAME,
        StudentStateChangeEntry.APPLY_DATE,
        StudentStateChangeEntry.CHANGED_CODE,
    ]
)
data class StudentStateChangeEntity(
    @ColumnInfo(name = StudentStateChangeEntry.USERNAME) val username: String,
    @ColumnInfo(name = StudentStateChangeEntry.APPLY_DATE) val applyDate: String,
    @ColumnInfo(name = StudentStateChangeEntry.CHANGED_DATE) val changedDate: String,
    @ColumnInfo(name = StudentStateChangeEntry.CHANGED_CODE) val changedCode: String,
    @ColumnInfo(name = StudentStateChangeEntry.CHANGED_REASON) val changedReason: String,
    @ColumnInfo(name = StudentStateChangeEntry.APPLIED_STATE_CODE) val applyStateCode: String,
) {
    override fun equals(other: Any?): Boolean {
        (other as? StudentStateChangeEntity)?.let {
            return (it.username == other.username) &&
                    (it.applyDate == other.applyDate) &&
                    (it.changedCode == other.changedCode)
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.username, this.applyDate, this.changedCode)
    }
}
