package com.konkuk.boost.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.konkuk.boost.persistence.area.SubjectAreaDao
import com.konkuk.boost.persistence.area.SubjectAreaEntity
import com.konkuk.boost.persistence.dept.DeptTransferDao
import com.konkuk.boost.persistence.dept.DeptTransferEntity
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
import com.konkuk.boost.persistence.stdstate.StudentStateChangeDao
import com.konkuk.boost.persistence.stdstate.StudentStateChangeEntity
import com.konkuk.boost.persistence.tuition.TuitionDao
import com.konkuk.boost.persistence.tuition.TuitionEntity

@Database(
    entities = [
        GraduationSimulationEntity::class,
        GradeEntity::class,
        LikeCourseEntity::class,
        RankEntity::class,
        SubjectAreaEntity::class,
        PersonalInfoEntity::class,
        DeptTransferEntity::class,
        StudentStateChangeEntity::class,
        TuitionEntity::class,
    ],
    version = 9,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun graduationSimulationDao(): GraduationSimulationDao

    abstract fun gradeDao(): GradeDao

    abstract fun likeCourseDao(): LikeCourseDao

    abstract fun rankDao(): RankDao

    abstract fun subjectAreaDao(): SubjectAreaDao

    abstract fun personalInfoDao(): PersonalInfoDao

    abstract fun deptTransferDao(): DeptTransferDao

    abstract fun studentStateChangeDao(): StudentStateChangeDao

    abstract fun tuitionDao(): TuitionDao
}