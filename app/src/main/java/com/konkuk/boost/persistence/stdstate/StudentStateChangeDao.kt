package com.konkuk.boost.persistence.stdstate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry.TABLE_NAME
import com.konkuk.boost.persistence.stdstate.StudentStateChangeContract.StudentStateChangeEntry.USERNAME

@Dao
interface StudentStateChangeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg studentStateChangeEntity: StudentStateChangeEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username")
    suspend fun getAll(username: String): List<StudentStateChangeEntity>
}