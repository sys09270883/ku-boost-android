package com.konkuk.boost.di

import com.konkuk.boost.api.AuthService
import com.konkuk.boost.api.CourseService
import com.konkuk.boost.api.GradeService
import com.konkuk.boost.api.LibraryService
import com.konkuk.boost.persistence.GradeDao
import com.konkuk.boost.persistence.GraduationSimulationDao
import com.konkuk.boost.persistence.PreferenceManager
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
        gradeDao: GradeDao
    ): GradeRepository =
        GradeRepositoryImpl(gradeService, graduationSimulationDao, preferenceManager, gradeDao)

    fun provideCourseRepository(
        courseService: CourseService,
        preferenceManager: PreferenceManager
    ): CourseRepository =
        CourseRepositoryImpl(courseService, preferenceManager)

    fun provideLibraryRepository(
        libraryService: LibraryService,
        preferenceManager: PreferenceManager
    ): LibraryRepository =
        LibraryResponseImpl(libraryService, preferenceManager)

    single { provideLoginRepository(get(), get()) }
    single { provideGradeRepository(get(), get(), get(), get()) }
    single { provideCourseRepository(get(), get()) }
    single { provideLibraryRepository(get(), get()) }
}