package com.konkuk.boost.repositories

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.konkuk.boost.api.AuthorizedKuisService
import com.konkuk.boost.api.KupisService
import com.konkuk.boost.data.course.RegistrationStatus
import com.konkuk.boost.data.course.SyllabusDetailResponse
import com.konkuk.boost.data.course.SyllabusResponse
import com.konkuk.boost.persistence.PreferenceManager
import com.konkuk.boost.persistence.like.LikeCourseDao
import com.konkuk.boost.persistence.like.LikeCourseEntity
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class CourseRepositoryImpl(
    private val authorizedKuisService: AuthorizedKuisService,
    private val preferenceManager: PreferenceManager,
    private val likeCourseDao: LikeCourseDao,
    private val kupisService: KupisService
) : CourseRepository {
    override suspend fun makeAllSyllabusRequest(
        year: Int,
        semester: Int
    ): UseCase<SyllabusResponse> {
        val syllabusResponse: SyllabusResponse
        try {
            withContext(Dispatchers.IO) {
                syllabusResponse = authorizedKuisService.fetchAllSyllabus(
                    year,
                    GradeUtils.convertToSemesterCode(semester)
                )
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(syllabusResponse)
    }

    override suspend fun makeDetailSyllabusRequest(
        year: Int,
        semester: Int,
        subjectId: String
    ): UseCase<SyllabusDetailResponse> {
        val syllabusDetailResponse: SyllabusDetailResponse
        try {
            withContext(Dispatchers.IO) {
                syllabusDetailResponse = authorizedKuisService.fetchSyllabus(
                    year,
                    GradeUtils.convertToSemesterCode(semester),
                    subjectId
                )
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(syllabusDetailResponse)
    }

    override fun setSemester(semester: Int): UseCase<Unit> {
        try {
            preferenceManager.selectedSemester = semester
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }
        return UseCase.success(Unit)
    }

    override fun getSemester(): UseCase<Int> {
        return UseCase.success(preferenceManager.selectedSemester)
    }

    override suspend fun insertLikeCourse(
        year: Int,
        semester: Int,
        subjectId: String,
        subjectName: String,
        professor: String,
        like: Boolean
    ): UseCase<Unit> {
        val username = preferenceManager.username

        try {
            withContext(Dispatchers.IO) {
                val course =
                    LikeCourseEntity(
                        username,
                        year,
                        semester,
                        subjectId,
                        subjectName,
                        professor,
                        like
                    )
                likeCourseDao.insertLikeCourse(course)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(Unit)
    }

    override suspend fun makeAllLikeCoursesRequest(
        year: Int,
        semester: Int
    ): UseCase<List<LikeCourseEntity>> {
        val username = preferenceManager.username
        val allLikeCourses: List<LikeCourseEntity>

        try {
            withContext(Dispatchers.IO) {
                allLikeCourses = likeCourseDao.getAllLikeCourses(username, year, semester)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(allLikeCourses)
    }

    override suspend fun isExist(
        year: Int,
        semester: Int,
        subjectId: String
    ): UseCase<LikeCourseEntity?> {
        val username = preferenceManager.username

        val likeCourse: LikeCourseEntity?
        try {
            withContext(Dispatchers.IO) {
                likeCourse = likeCourseDao.isExist(username, year, semester, subjectId)
            }
        } catch (e: Exception) {
            return UseCase.error("${e.message}")
        }

        return UseCase.success(likeCourse)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun makeCourseRegistrationStatusRequest(
        year: Int,
        semester: Int,
        subjectIdList: List<String>
    ): UseCase<List<List<RegistrationStatus>>> {
        val subjectList = mutableListOf<List<RegistrationStatus>>()

        try {
            withContext(Dispatchers.IO) {
                for (subjectId in subjectIdList) {
                    val list = mutableListOf<RegistrationStatus>()

                    for (promYear in 1..4) {
                        val semCode = GradeUtils.convertToSemesterCode(semester)
                        val response =
                            kupisService.getCourseRegistrationStatus(
                                year,
                                semCode,
                                promYear,
                                subjectId
                            )
                        val doc = Jsoup.parse(response.string())
                        val contents = doc.select("table tbody tr td")
                        val classBasketNumber = contents.first().text()
                        val (registrationNumber, limitedNumber) = contents.last().text().split("/")
                            .map { if (it.contains("null")) "0" else it.trim() }

                        list += RegistrationStatus(
                            classBasketNumber,
                            registrationNumber,
                            limitedNumber
                        )
                    }

                    subjectList.add(list)
                }
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(subjectList)
    }
}