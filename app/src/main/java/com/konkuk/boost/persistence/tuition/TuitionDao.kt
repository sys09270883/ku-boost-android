package com.konkuk.boost.persistence.tuition

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.TABLE_NAME
import com.konkuk.boost.persistence.tuition.TuitionContract.TuitionEntry.USERNAME

@Dao
interface TuitionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tuition: TuitionEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username")
    suspend fun getAll(username: String): List<TuitionEntity>
}