package com.konkuk.boost.persistence.grade

import androidx.room.*
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.CLASSIFICATION
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.SEMESTER
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.SUBJECT_ID
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.SUBJECT_NUMBER
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.TABLE_NAME
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.USERNAME
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.VALID
import com.konkuk.boost.persistence.grade.GradeContract.GradeEntry.YEAR

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

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username AND $CLASSIFICATION = :clf AND $VALID = :valid")
    suspend fun getGradesByClassification(
        username: String,
        clf: String,
        valid: Boolean = true
    ): List<GradeEntity>

    @Query("DELETE FROM $TABLE_NAME WHERE $USERNAME = :username AND $YEAR = :year AND $SEMESTER = :semester")
    suspend fun removeGrades(username: String, year: Int, semester: Int)

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username AND $SUBJECT_NUMBER = :subjectNumber")
    suspend fun getGradeBySubjectNumber(username: String, subjectNumber: String): GradeEntity

    @Transaction
    suspend fun updateClassificationBySubjectNumber(
        username: String,
        clf: String,
        subjectNumber: String,
        subjectArea: String = "",
    ) {
        val gradeEntity = getGradeBySubjectNumber(username, subjectNumber)
        gradeEntity.classification = clf

        if (subjectArea.isNotBlank())
            gradeEntity.subjectArea = subjectArea

        insertGrade(gradeEntity)
    }
}