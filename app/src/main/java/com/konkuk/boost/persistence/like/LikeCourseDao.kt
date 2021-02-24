package com.konkuk.boost.persistence.like

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konkuk.boost.persistence.like.LikeCourseContract.LikeCourseEntry

@Dao
interface LikeCourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikeCourse(vararg likeCourse: LikeCourseEntity)

    @Query(
        """
        SELECT * FROM ${LikeCourseEntry.TABLE_NAME} 
        WHERE ${LikeCourseEntry.USERNAME} = :username
        AND ${LikeCourseEntry.YEAR} = :year
        AND ${LikeCourseEntry.SEMESTER} = :semester
        AND ${LikeCourseEntry.LIKE} = :like
        """
    )
    suspend fun getAllLikeCourses(username: String, year: Int, semester: Int, like: Boolean = true): List<LikeCourseEntity>

    @Query(
        """
        SELECT * FROM ${LikeCourseEntry.TABLE_NAME}
        WHERE ${LikeCourseEntry.USERNAME} = :username 
        AND ${LikeCourseEntry.YEAR} = :year 
        AND ${LikeCourseEntry.SEMESTER} = :semester
        AND ${LikeCourseEntry.SUBJECT_ID} = :subjectId
        """
    )
    suspend fun isExist(username: String, year: Int, semester: Int, subjectId: String): LikeCourseEntity?
}