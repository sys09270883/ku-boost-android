package com.konkuk.boost.persistence.personal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.personal.PersonalInfoContract.TABLE_NAME

@Dao
interface PersonalInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg info: PersonalInfoEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username")
    suspend fun getAll(username: String): List<PersonalInfoEntity>
}