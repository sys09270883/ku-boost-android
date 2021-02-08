package com.konkuk.boost.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.RankContract.RankEntry.SEMESTER
import com.konkuk.boost.persistence.RankContract.RankEntry.TABLE_NAME
import com.konkuk.boost.persistence.RankContract.RankEntry.USERNAME
import com.konkuk.boost.persistence.RankContract.RankEntry.YEAR


@Dao
interface RankDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg rankEntity: RankEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username AND $YEAR = :year AND $SEMESTER = :semester")
    suspend fun getTotalRank(username: String, year: Int = 0, semester: Int = 0): RankEntity
}