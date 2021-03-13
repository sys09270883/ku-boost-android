package com.konkuk.boost.persistence.scholarship

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.TABLE_NAME

@Dao
interface ScholarshipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg scholarship: ScholarshipEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username")
    suspend fun getAll(username: String): List<ScholarshipEntity>
}