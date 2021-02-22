package com.konkuk.boost.di

import com.konkuk.boost.api.*
import com.konkuk.boost.persistence.area.SubjectAreaDao
import com.konkuk.boost.persistence.dept.DeptTransferDao
import com.konkuk.boost.persistence.grade.GradeDao
import com.konkuk.boost.persistence.like.LikeCourseDao
import com.konkuk.boost.persistence.personal.PersonalInfoDao
import com.konkuk.boost.persistence.pref.PreferenceManager
import com.konkuk.boost.persistence.rank.RankDao
import com.konkuk.boost.persistence.simul.GraduationSimulationDao
import com.konkuk.boost.persistence.stdstate.StudentStateChangeDao
import com.konkuk.boost.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    fun provideLoginRepository(
        kuisService: KuisService,
        preferenceManager: PreferenceManager,
        authorizedKuisService: AuthorizedKuisService,
        personalInfoDao: PersonalInfoDao,
        deptTransferDao: DeptTransferDao,
        studentStateChangeDao: StudentStateChangeDao,
    ): AuthRepository =
        AuthRepositoryImpl(
            kuisService,
            preferenceManager,
            authorizedKuisService,
            personalInfoDao,
            deptTransferDao,
            studentStateChangeDao
        )

    fun provideGradeRepository(
        authorizedKuisService: AuthorizedKuisService,
        graduationSimulationDao: GraduationSimulationDao,
        preferenceManager: PreferenceManager,
        gradeDao: GradeDao,
        rankDao: RankDao,
        ozService: OzService,
        subjectAreaDao: SubjectAreaDao
    ): GradeRepository =
        GradeRepositoryImpl(
            authorizedKuisService,
            graduationSimulationDao,
            preferenceManager,
            gradeDao,
            rankDao,
            ozService,
            subjectAreaDao
        )

    fun provideCourseRepository(
        authorizedKuisService: AuthorizedKuisService,
        preferenceManager: PreferenceManager,
        likeCourseDao: LikeCourseDao,
        kupisService: KupisService
    ): CourseRepository =
        CourseRepositoryImpl(authorizedKuisService, preferenceManager, likeCourseDao, kupisService)

    fun provideLibraryRepository(
        libraryService: LibraryService,
        preferenceManager: PreferenceManager
    ): LibraryRepository =
        LibraryResponseImpl(libraryService, preferenceManager)

    single { provideLoginRepository(get(), get(), get(), get(), get(), get()) }
    single { provideGradeRepository(get(), get(), get(), get(), get(), get(), get()) }
    single { provideCourseRepository(get(), get(), get(), get()) }
    single { provideLibraryRepository(get(), get()) }
}