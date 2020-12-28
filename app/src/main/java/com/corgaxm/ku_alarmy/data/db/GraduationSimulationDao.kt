package com.corgaxm.ku_alarmy.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.corgaxm.ku_alarmy.data.db.GraduationSimulationContract.GraduationSimulationEntry.TABLE_NAME
import com.corgaxm.ku_alarmy.data.db.GraduationSimulationContract.GraduationSimulationEntry.USERNAME
import kotlinx.coroutines.flow.Flow

@Dao
interface GraduationSimulationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGraduationSimulation(vararg graduationSimulationData: GraduationSimulationData)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username")
    fun loadGraduationSimulationByUsername(username: String): Flow<List<GraduationSimulationData>>
}