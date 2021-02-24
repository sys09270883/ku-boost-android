package com.konkuk.boost.persistence.tuition

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry
import java.util.*

@Entity(
    tableName = TuitionEntry.TABLE_NAME,
    primaryKeys = [
        TuitionEntry.USERNAME,
        TuitionEntry.PAID_DATE
    ]
)
data class TuitionEntity(
    @ColumnInfo(name = TuitionEntry.USERNAME) val username: String,
    @ColumnInfo(name = TuitionEntry.PAID_DATE) val paidDate: String,
    @ColumnInfo(name = TuitionEntry.TUITION_AMOUNT) val tuitionAmount: Int,
    @ColumnInfo(name = TuitionEntry.ENTER_AMOUNT) val enterAmount: Int,
    @ColumnInfo(name = TuitionEntry.YEAR) val year: Int,
    @ColumnInfo(name = TuitionEntry.SEMESTER) val semester: String,
    @ColumnInfo(name = TuitionEntry.STATE_CODE) val stateCode: String,
) {
    override fun equals(other: Any?): Boolean {
        (other as? TuitionEntity)?.let {
            return (it.username == other.username) &&
                    (it.paidDate == other.paidDate)
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.username, this.paidDate)
    }
}
