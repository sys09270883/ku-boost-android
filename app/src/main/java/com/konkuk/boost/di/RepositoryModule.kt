package com.konkuk.boost.di

import com.konkuk.boost.api.*
import com.konkuk.boost.persistence.*
import com.konkuk.boost.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    fun provideLoginRepository(
        authService: AuthService,
        preferenceManager: PreferenceManager
    ): AuthRepository = AuthRepositoryImpl(authService, preferenceManager)

    fun provideGradeRepository(
        gradeService: GradeService,
        graduationSimulationDao: GraduationSimulationDao,
        preferenceManager: PreferenceManager,
        gradeDao: GradeDao,
        rankDao: RankDao,
        ozService: OzService
    ): GradeRepository =
        GradeRepositoryImpl(
            gradeService,
            graduationSimulationDao,
            preferenceManager,
            gradeDao,
            rankDao,
            ozService
        )

    fun provideCourseRepository(
        courseService: CourseService,
        preferenceManager: PreferenceManager,
        likeCourseDao: LikeCourseDao
    ): CourseRepository =
        CourseRepositoryImpl(courseService, preferenceManager, likeCourseDao)

    fun provideLibraryRepository(
        libraryService: LibraryService,
        preferenceManager: PreferenceManager
    ): LibraryRepository =
        LibraryResponseImpl(libraryService, preferenceManager)

    single { provideLoginRepository(get(), get()) }
    single { provideGradeRepository(get(), get(), get(), get(), get(), get()) }
    single { provideCourseRepository(get(), get(), get()) }
    single { provideLibraryRepository(get(), get()) }
}