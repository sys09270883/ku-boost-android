package com.konkuk.boost.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.SubjectAreaContract.SubjectAreaEntry.TABLE_NAME
import com.konkuk.boost.persistence.SubjectAreaContract.SubjectAreaEntry.USERNAME

@Dao
interface SubjectAreaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg subjectArea: SubjectAreaEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username")
    suspend fun getAll(username: String): List<SubjectAreaEntity>
}