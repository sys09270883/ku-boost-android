package com.konkuk.boost.persistence.simul

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.AppContract.AppEntry.ACQUIRED
import com.konkuk.boost.persistence.AppContract.AppEntry.STANDARD
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.simul.GraduationSimulationContract.TABLE_NAME

@Dao
interface GraduationSimulationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGraduationSimulation(vararg graduationSimulationData: GraduationSimulationEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username ORDER BY $STANDARD ASC, $ACQUIRED ASC")
    fun loadGraduationSimulationByUsername(username: String): List<GraduationSimulationEntity>
}