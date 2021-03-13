package com.konkuk.boost.persistence.dept

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.dept.DeptTransferContract.TABLE_NAME

@Dao
interface DeptTransferDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg deptTransferEntity: DeptTransferEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username")
    suspend fun getAll(username: String): List<DeptTransferEntity>
}