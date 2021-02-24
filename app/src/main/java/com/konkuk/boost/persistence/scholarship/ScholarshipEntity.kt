package com.konkuk.boost.persistence.scholarship

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.DATE
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.ETC_AMOUNT
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.SCHOLARSHIP_ENTER_AMOUNT
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.SCHOLARSHIP_NAME
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.SCHOLARSHIP_TUITION_AMOUNT
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.SEMESTER
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.TABLE_NAME
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.USERNAME
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.YEAR
import java.util.*

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [
        USERNAME,
        SCHOLARSHIP_NAME
    ]
)
data class ScholarshipEntity(
    @ColumnInfo(name = USERNAME) val username: String,
    @ColumnInfo(name = SCHOLARSHIP_NAME) val scholarshipName: String,
    @ColumnInfo(name = SCHOLARSHIP_ENTER_AMOUNT) val scholarshipEnterAmount: Int,
    @ColumnInfo(name = SCHOLARSHIP_TUITION_AMOUNT) val scholarshipTuitionAmount: Int,
    @ColumnInfo(name = ETC_AMOUNT) val etcAmount: Int,
    @ColumnInfo(name = YEAR) val year: String,
    @ColumnInfo(name = SEMESTER) val semester: String,
    @ColumnInfo(name = DATE) val date: String,
) {
    override fun equals(other: Any?): Boolean {
        (other as? ScholarshipEntity)?.let {
            return (it.username == other.username) &&
                    (it.scholarshipName == other.scholarshipName)
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.username, this.scholarshipName)
    }
}
