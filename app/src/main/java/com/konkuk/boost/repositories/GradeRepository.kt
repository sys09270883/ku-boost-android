package com.konkuk.boost.repositories

import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.data.grade.GraduationSimulationResponse
import com.konkuk.boost.data.grade.UserInformationResponse
import com.konkuk.boost.persistence.GradeEntity
import com.konkuk.boost.persistence.GraduationSimulationEntity

interface GradeRepository {
    suspend fun makeGraduationSimulationRequest(): UseCase<GraduationSimulationResponse>

    suspend fun makeUserInformationRequest(): UseCase<UserInformationResponse>

    suspend fun getGraduationSimulations(): UseCase<List<GraduationSimulationEntity>>

    fun getStdNo(): Int

    suspend fun makeAllGradesRequest(): UseCase<Unit>

    suspend fun makeAllValidGradesRequest(): UseCase<Unit>

    suspend fun getAllValidGrades(): UseCase<List<GradeEntity>>

    suspend fun getCurrentGrades(): UseCase<List<GradeEntity>>

    fun hasData(): Boolean
}