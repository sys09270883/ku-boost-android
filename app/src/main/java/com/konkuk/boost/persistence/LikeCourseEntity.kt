package com.konkuk.boost.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.LikeCourseContract.LikeCourseEntry
import java.util.*

@Entity(
    tableName = LikeCourseEntry.TABLE_NAME,
    primaryKeys = [LikeCourseEntry.USERNAME, LikeCourseEntry.YEAR, LikeCourseEntry.SEMESTER, LikeCourseEntry.SUBJECT_ID]
)
data class LikeCourseEntity(
    @ColumnInfo(name = LikeCourseEntry.USERNAME) val username: String,
    @ColumnInfo(name = LikeCourseEntry.YEAR) val year: Int,
    @ColumnInfo(name = LikeCourseEntry.SEMESTER) val semester: Int,
    @ColumnInfo(name = LikeCourseEntry.SUBJECT_ID) val subjectId: String,
    @ColumnInfo(name = LikeCourseEntry.LIKE) val like: Boolean,
) {

    override fun equals(other: Any?): Boolean {
        (other as? LikeCourseEntity)?.let {
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