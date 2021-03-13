package com.konkuk.boost.persistence.tuition

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.AppContract.AppEntry.ENTER_AMOUNT
import com.konkuk.boost.persistence.AppContract.AppEntry.PAID_DATE
import com.konkuk.boost.persistence.AppContract.AppEntry.SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.STATE_CODE
import com.konkuk.boost.persistence.AppContract.AppEntry.TUITION_AMOUNT
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.YEAR
import java.util.*

@Entity(
    tableName = TuitionContract.TABLE_NAME,
    primaryKeys = [USERNAME, PAID_DATE]
)
data class TuitionEntity(
    @ColumnInfo(name = USERNAME) val username: String,
    @ColumnInfo(name = PAID_DATE) val paidDate: String,
    @ColumnInfo(name = TUITION_AMOUNT) val tuitionAmount: Int,
    @ColumnInfo(name = ENTER_AMOUNT) val enterAmount: Int,
    @ColumnInfo(name = YEAR) val year: Int,
    @ColumnInfo(name = SEMESTER) val semester: String,
    @ColumnInfo(name = STATE_CODE) val stateCode: String,
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
