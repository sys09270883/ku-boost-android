package com.konkuk.boost.di

import com.konkuk.boost.api.AuthorizedKuisService
import com.konkuk.boost.api.KuisService
import com.konkuk.boost.api.LibraryService
import com.konkuk.boost.api.OzService
import com.konkuk.boost.persistence.PreferenceManager
import com.konkuk.boost.persistence.area.SubjectAreaDao
import com.konkuk.boost.persistence.dept.DeptTransferDao
import com.konkuk.boost.persistence.grade.GradeDao
import com.konkuk.boost.persistence.personal.PersonalInfoDao
import com.konkuk.boost.persistence.rank.RankDao
import com.konkuk.boost.persistence.scholarship.ScholarshipDao
import com.konkuk.boost.persistence.simul.GraduationSimulationDao
import com.konkuk.boost.persistence.stdstate.StudentStateChangeDao
import com.konkuk.boost.persistence.tuition.TuitionDao
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
        tuitionDao: TuitionDao,
        scholarshipDao: ScholarshipDao,
    ): AuthRepository =
        AuthRepositoryImpl(
            kuisService,
            preferenceManager,
            authorizedKuisService,
            personalInfoDao,
            deptTransferDao,
            studentStateChangeDao,
            tuitionDao,
            scholarshipDao,
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

    fun provideLibraryRepository(
        libraryService: LibraryService,
        preferenceManager: PreferenceManager
    ): LibraryRepository =
        LibraryResponseImpl(libraryService, preferenceManager)

    single { provideLoginRepository(get(), get(), get(), get(), get(), get(), get(), get()) }
    single { provideGradeRepository(get(), get(), get(), get(), get(), get(), get()) }
    single { provideLibraryRepository(get(), get()) }
}