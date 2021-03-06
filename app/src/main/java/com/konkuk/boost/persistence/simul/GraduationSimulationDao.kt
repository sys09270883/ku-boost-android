package com.konkuk.boost.persistence.simul

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.simul.GraduationSimulationContract.GraduationSimulationEntry.ACQUIRED
import com.konkuk.boost.persistence.simul.GraduationSimulationContract.GraduationSimulationEntry.STANDARD
import com.konkuk.boost.persistence.simul.GraduationSimulationContract.GraduationSimulationEntry.TABLE_NAME
import com.konkuk.boost.persistence.simul.GraduationSimulationContract.GraduationSimulationEntry.USERNAME

@Dao
interface GraduationSimulationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGraduationSimulation(vararg graduationSimulationData: GraduationSimulationEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username ORDER BY $STANDARD ASC, $ACQUIRED ASC")
    fun loadGraduationSimulationByUsername(username: String): List<GraduationSimulationEntity>
}