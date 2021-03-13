package com.konkuk.boost.persistence.dept

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.AppContract.AppEntry.BEFORE_DEPT
import com.konkuk.boost.persistence.AppContract.AppEntry.BEFORE_MAJOR
import com.konkuk.boost.persistence.AppContract.AppEntry.BEFORE_SUST
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_CODE
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_DATE
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.CHANGED_YEAR
import com.konkuk.boost.persistence.AppContract.AppEntry.DEPT
import com.konkuk.boost.persistence.AppContract.AppEntry.MAJOR
import com.konkuk.boost.persistence.AppContract.AppEntry.SUST
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import java.util.*

@Entity(
    tableName = DeptTransferContract.TABLE_NAME,
    primaryKeys = [
        USERNAME,
        CHANGED_CODE,
        CHANGED_DATE,
    ]
)
data class DeptTransferEntity(
    @ColumnInfo(name = USERNAME)
    val username: String,
    @ColumnInfo(name = BEFORE_DEPT)
    val beforeDept: String,
    @ColumnInfo(name = BEFORE_SUST)
    val beforeSust: String,
    @ColumnInfo(name = BEFORE_MAJOR)
    val beforeMajor: String,
    @ColumnInfo(name = CHANGED_CODE)
    val changedCode: String,
    @ColumnInfo(name = CHANGED_DATE)
    val changedDate: String,
    @ColumnInfo(name = CHANGED_YEAR)
    val changedYear: String,
    @ColumnInfo(name = CHANGED_SEMESTER)
    val changedSemester: String,
    @ColumnInfo(name = DEPT)
    val dept: String,
    @ColumnInfo(name = SUST)
    val sust: String,
    @ColumnInfo(name = MAJOR)
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