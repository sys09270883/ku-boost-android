package com.konkuk.boost.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface LikeCourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikeCourse(vararg likeCourse: LikeCourseEntity)
}