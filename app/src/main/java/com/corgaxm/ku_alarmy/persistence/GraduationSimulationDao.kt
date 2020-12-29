package com.corgaxm.ku_alarmy.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationContract.GraduationSimulationEntry.ACQUIRED
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationContract.GraduationSimulationEntry.STANDARD
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationContract.GraduationSimulationEntry.TABLE_NAME
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationContract.GraduationSimulationEntry.USERNAME
import kotlinx.coroutines.flow.Flow

@Dao
interface GraduationSimulationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGraduationSimulation(vararg graduationSimulationData: GraduationSimulationEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username ORDER BY $STANDARD ASC, $ACQUIRED ASC")
    fun loadGraduationSimulationByUsername(username: String): Flow<List<GraduationSimulationEntity>>
}