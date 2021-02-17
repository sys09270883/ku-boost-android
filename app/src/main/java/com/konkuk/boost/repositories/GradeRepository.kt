package com.konkuk.boost.repositories

import com.konkuk.boost.data.grade.GraduationSimulationResponse
import com.konkuk.boost.data.grade.SubjectAreaCount
import com.konkuk.boost.data.grade.UserInformationResponse
import com.konkuk.boost.persistence.grade.GradeEntity
import com.konkuk.boost.persistence.simul.GraduationSimulationEntity
import com.konkuk.boost.persistence.rank.RankEntity
import com.konkuk.boost.persistence.area.SubjectAreaEntity
import com.konkuk.boost.utils.UseCase

interface GradeRepository {
    suspend fun makeGraduationSimulationRequest(): UseCase<GraduationSimulationResponse>

    suspend fun makeUserInformationRequest(): UseCase<UserInformationResponse>

    suspend fun getGraduationSimulations(): UseCase<List<GraduationSimulationEntity>>

    fun getStdNo(): Int

    suspend fun makeValidGradesAndUpdateClassification(): UseCase<Unit>

    suspend fun getAllValidGrades(): UseCase<List<GradeEntity>>

    suspend fun getCurrentGrades(): UseCase<List<GradeEntity>>

    fun hasData(): Boolean

    suspend fun getGradesByClassification(clf: String): UseCase<List<GradeEntity>>

    suspend fun getTotalRank(year: Int, semester: Int): UseCase<RankEntity>

    suspend fun makeTotalRank(): UseCase<Unit>

    suspend fun getAllSubjectArea(): UseCase<List<SubjectAreaEntity>>

    suspend fun getSubjectAreaCounts(): UseCase<List<SubjectAreaCount>>
}