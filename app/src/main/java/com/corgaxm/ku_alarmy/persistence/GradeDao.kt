package com.corgaxm.ku_alarmy.persistence

import androidx.room.*
import com.corgaxm.ku_alarmy.persistence.GradeContract.GradeEntry.SEMESTER
import com.corgaxm.ku_alarmy.persistence.GradeContract.GradeEntry.SUBJECT_ID
import com.corgaxm.ku_alarmy.persistence.GradeContract.GradeEntry.TABLE_NAME
import com.corgaxm.ku_alarmy.persistence.GradeContract.GradeEntry.USERNAME
import com.corgaxm.ku_alarmy.persistence.GradeContract.GradeEntry.VALID
import com.corgaxm.ku_alarmy.persistence.GradeContract.GradeEntry.YEAR

@Dao
interface GradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(vararg gradeEntity: GradeEntity)

    @Query("UPDATE $TABLE_NAME SET $VALID = :valid WHERE $USERNAME = :username AND $SUBJECT_ID = :subjectId")
    suspend fun updateValid(username: String, subjectId: String, valid: Boolean)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username AND $VALID = :valid ORDER BY $YEAR, $SEMESTER")
    suspend fun getAllGrades(username: String, valid: Boolean = true): List<GradeEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username ORDER BY $YEAR DESC, $SEMESTER DESC LIMIT 1")
    suspend fun getCurrentSemester(username: String): GradeEntity

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username AND $YEAR = :year AND $SEMESTER = :semester")
    suspend fun getCurrentSemesterGrades(
        username: String,
        year: Int,
        semester: Int
    ): List<GradeEntity>

    @Transaction
    suspend fun getCurrentSemesterGradesTransaction(username: String): List<GradeEntity> {
        val gradeEntity = getCurrentSemester(username)
        return getCurrentSemesterGrades(username, gradeEntity.year, gradeEntity.semester)
    }
}