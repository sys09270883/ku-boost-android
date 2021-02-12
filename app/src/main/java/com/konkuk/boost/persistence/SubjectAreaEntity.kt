package com.konkuk.boost.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.konkuk.boost.persistence.SubjectAreaContract.SubjectAreaEntry

@Entity(
    tableName = SubjectAreaEntry.TABLE_NAME,
    primaryKeys = [SubjectAreaEntry.USERNAME, SubjectAreaEntry.SUBJECT_AREA_NAME]
)
data class SubjectAreaEntity(
    @ColumnInfo(name = SubjectAreaEntry.USERNAME)
    val username: String,
    @ColumnInfo(name = SubjectAreaEntry.TYPE)
    val type: Int,  // 0: Default, 1: 기교, 2: 심교/핵교
    @ColumnInfo(name = SubjectAreaEntry.SUBJECT_AREA_NAME)
    val subjectAreaName: String,
)