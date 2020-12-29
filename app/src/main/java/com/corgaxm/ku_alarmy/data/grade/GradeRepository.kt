package com.corgaxm.ku_alarmy.data.grade

import com.corgaxm.ku_alarmy.data.UseCase
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationEntity
import kotlinx.coroutines.flow.Flow

interface GradeRepository {

    suspend fun makeGraduationSimulationRequest(): UseCase<GraduationSimulationResponse>

    suspend fun makeUserInformationRequest(): UseCase<UserInformationResponse>

    suspend fun getGraduationSimulations(): UseCase<List<GraduationSimulationEntity>>

    suspend fun setGraduationSimulation(graduationSimulationEntity: GraduationSimulationEntity): UseCase<Unit>

    fun getStdNoFlow(): Flow<Int>
}