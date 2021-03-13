package com.konkuk.boost.persistence.grade

import androidx.room.*
import com.konkuk.boost.persistence.AppContract.AppEntry.CLASSIFICATION
import com.konkuk.boost.persistence.AppContract.AppEntry.SEMESTER
import com.konkuk.boost.persistence.AppContract.AppEntry.SUBJECT_NUMBER
import com.konkuk.boost.persistence.AppContract.AppEntry.TYPE
import com.konkuk.boost.persistence.AppContract.AppEntry.USERNAME
import com.konkuk.boost.persistence.AppContract.AppEntry.YEAR
import com.konkuk.boost.persistence.grade.GradeContract.TABLE_NAME

@Dao
interface GradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(vararg gradeEntity: GradeEntity)

    @Query(
        """
        UPDATE $TABLE_NAME SET $TYPE = :type 
        WHERE $USERNAME = :username 
        AND $SUBJECT_NUMBER = :subjectNumber
        AND $YEAR = :year
        AND $SEMESTER = :semester
    """
    )
    suspend fun updateType(
        username: String,
        subjectNumber: String,
        type: Int,
        year: Int,
        semester: Int
    )

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username AND $TYPE = :type ORDER BY $YEAR, $SEMESTER")
    suspend fun getAllValidGrades(username: String, type: Int = 0): List<GradeEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username AND $TYPE < 2 ORDER BY $YEAR, $SEMESTER")
    suspend fun getNotDeletedGrades(username: String): List<GradeEntity>

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

    @Query("SELECT * FROM $TABLE_NAME WHERE $USERNAME = :username AND $CLASSIFICATION = :clf AND $TYPE < 2")
    suspend fun getNotDeletedGradesByClassification(
        username: String,
        clf: String
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