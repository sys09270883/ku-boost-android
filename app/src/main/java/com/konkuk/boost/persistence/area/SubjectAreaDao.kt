package com.konkuk.boost.persistence.area

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.area.SubjectAreaContract.TABLE_NAME

@Dao
interface SubjectAreaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg subjectArea: SubjectAreaEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username")
    suspend fun getAll(username: String): List<SubjectAreaEntity>
}