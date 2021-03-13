package com.konkuk.boost.persistence.grade

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.AppContract.AppEntry.CHARACTER_GRADE
import com.konkuk.boost.persistence.AppContract.AppEntry.CLASSIFICATION
import com.konkuk.boost.persistence.AppContract.AppEntry.EVALUATION_METHOD
import com.konkuk.boost.persistence.AppContract.AppEntry.GRADE
import com.konkuk.boost.persistence.AppContract.AppEntry.PROFESSOR
import com.konkuk.boost.persistence.AppContract.AppEntry.SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_AREA
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_ID
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_NAME
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_NUMBER
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_POINT
import com.konkuk.boost.persistence.AppContract.AppEntry.TYPE
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.YEAR
import java.util.*

@Entity(
    tableName = GradeContract.TABLE_NAME,
    primaryKeys = [USERNAME, SUBJECT_ID, SEMESTER, YEAR]
)
data class GradeEntity(
    @ColumnInfo(name = USERNAME) val username: String,
    @ColumnInfo(name = EVALUATION_METHOD) val evaluationMethod: String,
    @ColumnInfo(name = YEAR) val year: Int,
    @ColumnInfo(name = SEMESTER) val semester: Int,
    @ColumnInfo(name = CLASSIFICATION) var classification: String,
    @ColumnInfo(name = CHARACTER_GRADE) val characterGrade: String,
    @ColumnInfo(name = GRADE) val grade: Float,
    @ColumnInfo(name = PROFESSOR) val professor: String,
    @ColumnInfo(name = SUBJECT_ID) val subjectId: String,
    @ColumnInfo(name = SUBJECT_NAME) val subjectName: String,
    @ColumnInfo(name = SUBJECT_NUMBER) val subjectNumber: String,
    @ColumnInfo(name = SUBJECT_POINT) val subjectPoint: Int,
    @ColumnInfo(name = SUBJECT_AREA) var subjectArea: String,
    @ColumnInfo(name = TYPE) var type: Int,  // 0: valid, 1: pending, 2: deleted
) {

    override fun equals(other: Any?): Boolean {
        (other as? GradeEntity)?.let {
            return (it.username == other.username) &&
                    (it.year == other.year) &&
                    (it.semester == other.semester) &&
                    (it.subjectId == other.subjectId)
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.username, this.year, this.semester, this.subjectId)
    }
}