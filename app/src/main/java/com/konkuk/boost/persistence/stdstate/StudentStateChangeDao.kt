package com.konkuk.boost.persistence.stdstate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.TABLE_NAME

@Dao
interface StudentStateChangeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg studentStateChangeEntity: StudentStateChangeEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username")
    suspend fun getAll(username: String): List<StudentStateChangeEntity>
}