package com.konkuk.boost.persistence.stdstate

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.AppContract.AppEntry.APPLIED_STATE_CODE
import com.konkuk.boost.persistence.AppContract.AppEntry.APPLY_DATE
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_CODE
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_DATE
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_REASON
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import java.util.*

@Entity(
    tableName = StudentStateChangeContract.TABLE_NAME,
    primaryKeys = [USERNAME, APPLY_DATE, CHANGED_CODE]
)
data class StudentStateChangeEntity(
    @ColumnInfo(name = USERNAME) val username: String,
    @ColumnInfo(name = APPLY_DATE) val applyDate: String,
    @ColumnInfo(name = CHANGED_DATE) val changedDate: String,
    @ColumnInfo(name = CHANGED_CODE) val changedCode: String,
    @ColumnInfo(name = CHANGED_REASON) val changedReason: String,
    @ColumnInfo(name = APPLIED_STATE_CODE) val applyStateCode: String,
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
