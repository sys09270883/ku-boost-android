package com.konkuk.boost.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.konkuk.boost.persistence.area.SubjectAreaDao
import com.konkuk.boost.persistence.area.SubjectAreaEntity
import com.konkuk.boost.persistence.grade.GradeDao
import com.konkuk.boost.persistence.grade.GradeEntity
import com.konkuk.boost.persistence.like.LikeCourseDao
import com.konkuk.boost.persistence.like.LikeCourseEntity
import com.konkuk.boost.persistence.personal.PersonalInfoDao
import com.konkuk.boost.persistence.personal.PersonalInfoEntity
import com.konkuk.boost.persistence.rank.RankDao
import com.konkuk.boost.persistence.rank.RankEntity
import com.konkuk.boost.persistence.simul.GraduationSimulationDao
import com.konkuk.boost.persistence.simul.GraduationSimulationEntity

@Database(
    entities = [
        GraduationSimulationEntity::class,
        GradeEntity::class,
        LikeCourseEntity::class,
        RankEntity::class,
        SubjectAreaEntity::class,
        PersonalInfoEntity::class,
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun graduationSimulationDao(): GraduationSimulationDao

    abstract fun gradeDao(): GradeDao

    abstract fun likeCourseDao(): LikeCourseDao

    abstract fun rankDao(): RankDao

    abstract fun subjectAreaDao(): SubjectAreaDao

    abstract fun personalInfoDao(): PersonalInfoDao
}