package com.konkuk.boost.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.GradeContract.GradeEntry
import java.util.*

@Entity(
    tableName = GradeEntry.TABLE_NAME,
    primaryKeys = [GradeEntry.USERNAME, GradeEntry.SUBJECT_ID, GradeEntry.SEMESTER, GradeEntry.YEAR]
)
data class GradeEntity(
    @ColumnInfo(name = GradeEntry.USERNAME) val username: String,
    @ColumnInfo(name = GradeEntry.EVALUATION_METHOD) val evaluationMethod: String,
    @ColumnInfo(name = GradeEntry.YEAR) val year: Int,
    @ColumnInfo(name = GradeEntry.SEMESTER) val semester: Int,
    @ColumnInfo(name = GradeEntry.CLASSIFICATION) var classification: String,
    @ColumnInfo(name = GradeEntry.CHARACTER_GRADE) val characterGrade: String,
    @ColumnInfo(name = GradeEntry.GRADE) val grade: Float,
    @ColumnInfo(name = GradeEntry.PROFESSOR) val professor: String,
    @ColumnInfo(name = GradeEntry.SUBJECT_ID) val subjectId: String,
    @ColumnInfo(name = GradeEntry.SUBJECT_NAME) val subjectName: String,
    @ColumnInfo(name = GradeEntry.SUBJECT_NUMBER) val subjectNumber: String,
    @ColumnInfo(name = GradeEntry.SUBJECT_POINT) val subjectPoint: Int,
    @ColumnInfo(name = GradeEntry.SUBJECT_AREA) var subjectArea: String,
    @ColumnInfo(name = GradeEntry.VALID) var valid: Boolean,
    @ColumnInfo(
        name = GradeEntry.MODIFIED_AT,
        defaultValue = "CURRENT_TIMESTAMP"
    ) val modifiedAt: Long,
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