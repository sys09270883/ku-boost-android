package com.corgaxm.ku_alarmy.data.db

import com.corgaxm.ku_alarmy.utils.Resource

interface GradeRepository {
    suspend fun getGraduationSimulations(): Resource<List<GraduationSimulationData>>
    suspend fun setGraduationSimulations(
        standard: GraduationSimulationData,
        acquired: GraduationSimulationData
    ): Resource<Unit>
}