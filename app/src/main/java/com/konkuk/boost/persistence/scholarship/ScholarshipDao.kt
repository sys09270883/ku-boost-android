package com.konkuk.boost.persistence.scholarship

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.TABLE_NAME
import com.konkuk.boost.persistence.scholarship.ScholarshipContract.ScholarshipEntry.USERNAME

@Dao
interface ScholarshipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg scholarship: ScholarshipEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username")
    suspend fun getAll(username: String): List<ScholarshipEntity>
}