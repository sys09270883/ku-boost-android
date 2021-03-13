package com.konkuk.boost.persistence.area

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_AREA_NAME
import com.konkuk.boost.persistence.AppContract.AppEntry.TYPE
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import java.util.*

@Entity(
    tableName = SubjectAreaContract.TABLE_NAME,
    primaryKeys = [USERNAME, SUBJECT_AREA_NAME]
)
data class SubjectAreaEntity(
    @ColumnInfo(name = USERNAME)
    val username: String,
    @ColumnInfo(name = TYPE)
    val type: Int,  // 0: Default, 1: 기교, 2: 심교/핵교
    @ColumnInfo(name = SUBJECT_AREA_NAME)
    val subjectAreaName: String,
) {
    override fun equals(other: Any?): Boolean {
        (other as? SubjectAreaEntity)?.let {
            return (it.username == other.username) &&
                    (it.subjectAreaName == other.subjectAreaName)
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.username, this.subjectAreaName)
    }

    enum class AreaType {
        Default, Basic, Core
    }
}