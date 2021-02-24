package com.konkuk.boost.persistence.dept

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.dept.DeptTransferContract.DeptTransferEntry
import java.util.*

@Entity(
    tableName = DeptTransferEntry.TABLE_NAME,
    primaryKeys = [
        DeptTransferEntry.USERNAME,
        DeptTransferEntry.CHANGED_CODE,
        DeptTransferEntry.CHANGED_DATE,
    ]
)
data class DeptTransferEntity(
    @ColumnInfo(name = DeptTransferEntry.USERNAME)
    val username: String,
    @ColumnInfo(name = DeptTransferEntry.BEFORE_DEPT)
    val beforeDept: String,
    @ColumnInfo(name = DeptTransferEntry.BEFORE_SUST)
    val beforeSust: String,
    @ColumnInfo(name = DeptTransferEntry.BEFORE_MAJOR)
    val beforeMajor: String,
    @ColumnInfo(name = DeptTransferEntry.CHANGED_CODE)
    val changedCode: String,
    @ColumnInfo(name = DeptTransferEntry.CHANGED_DATE)
    val changedDate: String,
    @ColumnInfo(name = DeptTransferEntry.CHANGED_YEAR)
    val changedYear: String,
    @ColumnInfo(name = DeptTransferEntry.CHANGED_SEMESTER)
    val changedSemester: String,
    @ColumnInfo(name = DeptTransferEntry.DEPT)
    val dept: String,
    @ColumnInfo(name = DeptTransferEntry.SUST)
    val sust: String,
    @ColumnInfo(name = DeptTransferEntry.MAJOR)
    val major: String,
) {
    override fun equals(other: Any?): Boolean {
        (other as? DeptTransferEntity)?.let {
            return (it.username == other.username) &&
                    (it.changedCode == other.changedCode) &&
                    (it.changedDate == other.changedDate)
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.username, this.changedCode, this.changedDate)
    }
}