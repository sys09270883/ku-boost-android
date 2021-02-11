package com.konkuk.boost.di

import com.konkuk.boost.api.*
import com.konkuk.boost.persistence.*
import com.konkuk.boost.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    fun provideLoginRepository(
        kuisService: KuisService,
        preferenceManager: PreferenceManager
    ): AuthRepository = AuthRepositoryImpl(kuisService, preferenceManager)

    fun provideGradeRepository(
        authorizedKuisService: AuthorizedKuisService,
        graduationSimulationDao: GraduationSimulationDao,
        preferenceManager: PreferenceManager,
        gradeDao: GradeDao,
        rankDao: RankDao,
        ozService: OzService
    ): GradeRepository =
        GradeRepositoryImpl(
            authorizedKuisService,
            graduationSimulationDao,
            preferenceManager,
            gradeDao,
            rankDao,
            ozService
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

    single { provideLoginRepository(get(), get()) }
    single { provideGradeRepository(get(), get(), get(), get(), get(), get()) }
    single { provideCourseRepository(get(), get(), get(), get()) }
    single { provideLibraryRepository(get(), get()) }
}