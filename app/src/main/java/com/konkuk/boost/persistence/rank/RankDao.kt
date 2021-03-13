package com.konkuk.boost.persistence.rank

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.AppContract.AppEntry.SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.YEAR
import com.konkuk.boost.persistence.rank.RankContract.TABLE_NAME


@Dao
interface RankDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg rankEntity: RankEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username AND $YEAR = :year AND $SEMESTER = :semester")
    suspend fun get(username: String, year: Int = 0, semester: Int = 0): RankEntity
}