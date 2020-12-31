package com.corgaxm.ku_alarmy.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
}