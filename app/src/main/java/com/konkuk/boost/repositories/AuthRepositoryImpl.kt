package com.konkuk.boost.repositories

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.konkuk.boost.api.AuthorizedKuisService
import com.konkuk.boost.api.KuisService
import com.konkuk.boost.data.auth.ChangePasswordResponse
import com.konkuk.boost.data.auth.LoginResponse
import com.konkuk.boost.data.auth.StudentInfoResponse
import com.konkuk.boost.persistence.PreferenceManager
import com.konkuk.boost.persistence.dept.DeptTransferDao
import com.konkuk.boost.persistence.dept.DeptTransferEntity
import com.konkuk.boost.persistence.personal.PersonalInfoDao
import com.konkuk.boost.persistence.personal.PersonalInfoEntity
import com.konkuk.boost.persistence.scholarship.ScholarshipDao
import com.konkuk.boost.persistence.scholarship.ScholarshipEntity
import com.konkuk.boost.persistence.stdstate.StudentStateChangeDao
import com.konkuk.boost.persistence.stdstate.StudentStateChangeEntity
import com.konkuk.boost.persistence.tuition.TuitionDao
import com.konkuk.boost.persistence.tuition.TuitionEntity
import com.konkuk.boost.utils.MessageUtils
import com.konkuk.boost.utils.UseCase
import retrofit2.Response

class AuthRepositoryImpl(
    private val kuisService: KuisService,
    private val preferenceManager: PreferenceManager,
    private val authorizedKuisService: AuthorizedKuisService,
    private val personalInfoDao: PersonalInfoDao,
    private val deptTransferDao: DeptTransferDao,
    private val studentStateChangeDao: StudentStateChangeDao,
    private val tuitionDao: TuitionDao,
    private val scholarshipDao: ScholarshipDao,
) : AuthRepository {
    override suspend fun makeLoginRequest(
        username: String,
        password: String
    ): UseCase<LoginResponse> {
        val loginResponse: Response<LoginResponse>
        try {
            loginResponse = kuisService.login(username, password)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error(MessageUtils.ERROR_ON_SERVER)
        }

        val cookie = loginResponse.headers()["Set-Cookie"]?.split(";")?.first()
            ?: return UseCase.error(MessageUtils.LOGIN_AGAIN)
        preferenceManager.cookie = cookie

        val loginBody = loginResponse.body()

        val loginSuccess = loginBody?.loginSuccess
        val loginFailure = loginBody?.loginFailure

        return when {
            loginSuccess?.isSucceeded == true -> {
                preferenceManager.setAuthInfo(username, password)
                UseCase.success(loginBody)
            }
            loginSuccess == null && loginFailure != null -> UseCase.error(
                loginFailure.errorMessage,
                loginBody
            )
            else -> UseCase.error(MessageUtils.ERROR_ON_SERVER)
        }
    }

    override suspend fun makeAutoLoginRequest(): UseCase<LoginResponse> {
        val username = preferenceManager.username
        val password = preferenceManager.password
        return makeLoginRequest(username, password)
    }

    override suspend fun makeLogoutRequest(): UseCase<Unit> {
        preferenceManager.clearAll()
        return UseCase.success(Unit)
    }

    override fun getUsername() = preferenceManager.username

    override fun getPassword() = preferenceManager.password

    override fun getName() = preferenceManager.name

    override fun getDept() = preferenceManager.dept

    override fun getStdNo() = preferenceManager.stdNo

    override fun getState() = preferenceManager.state

    override suspend fun setPassword(password: String) {
        preferenceManager.password = password
    }

    override suspend fun makeChangePasswordRequest(
        username: String,
        password: String
    ): UseCase<ChangePasswordResponse> {
        val changePasswordResponse: ChangePasswordResponse

        try {
            changePasswordResponse = kuisService.changePasswordAfter90Days(username, password)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return when (changePasswordResponse.response.flag) {
            "1" -> UseCase.error(MessageUtils.INCORRECT_PASSWORD)
            "3" -> UseCase.success(changePasswordResponse, MessageUtils.CHANGE_PASSWORD_SUCCESSFULLY)
            "PASS" -> UseCase.success(changePasswordResponse, MessageUtils.CHANGE_PASSWORD_AFTER_90_DAYS)
            else -> UseCase.error(MessageUtils.ERROR_ON_SERVER)
        }
    }

    override suspend fun makeChangePasswordRequest(
        username: String,
        beforePassword: String,
        password: String,
        password2: String,
        procDiv: String
    ): UseCase<ChangePasswordResponse> {
        val changePasswordResponse: ChangePasswordResponse

        try {
            changePasswordResponse = kuisService.changePasswordAfter90Days(
                username,
                beforePassword,
                password,
                password2,
                procDiv
            )
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            return UseCase.error("${e.message}")
        }

        return when (changePasswordResponse.response.flag) {
            "1" -> UseCase.error(MessageUtils.INCORRECT_PASSWORD)
            "3" -> UseCase.success(changePasswordResponse, MessageUtils.CHANGE_PASSWORD_SUCCESSFULLY)
            "PASS" -> UseCase.success(changePasswordResponse, MessageUtils.CHANGE_PASSWORD_AFTER_90_DAYS)
            else -> UseCase.error(MessageUtils.ERROR_ON_SERVER)
        }
    }

    override suspend fun makeStudentInfoRequest(): UseCase<StudentInfoResponse> {
        val stdNo = preferenceManager.stdNo
        val username = preferenceManager.username
        val studentInfoResponse: StudentInfoResponse

        try {
            studentInfoResponse = authorizedKuisService.fetchStudentInfo(stdNo)

            // Fetch department transfer information and insert into table.
            val deptTransferInfoList = studentInfoResponse.deptTransferInfo
            val deptTransferEntities = mutableListOf<DeptTransferEntity>()

            for (info in deptTransferInfoList) {
                deptTransferEntities += DeptTransferEntity(
                    username,
                    info.beforeDept ?: "",
                    info.beforeSust ?: "",
                    info.beforeMajor ?: "",
                    info.changedCode,
                    info.changedDate,
                    info.changedYear,
                    info.changedSemester,
                    info.dept ?: "",
                    info.sust ?: "",
                    info.major ?: ""
                )
            }

            deptTransferDao.insert(*deptTransferEntities.toTypedArray())


            // Fetch student state change information and insert into table.
            val studentStateChangeInfoList = studentInfoResponse.studentStateChangeInfo
            val studentStateChangeEntities = mutableListOf<StudentStateChangeEntity>()

            for (info in studentStateChangeInfoList) {
                studentStateChangeEntities += StudentStateChangeEntity(
                    username,
                    info.applyDate,
                    info.changedDate,
                    info.changedCode,
                    info.changedReason ?: "",
                    info.appliedStateCode
                )
            }

            studentStateChangeDao.insert(*studentStateChangeEntities.toTypedArray())


            // Fetch personal information and insert into table.
            val personalInfo = studentInfoResponse.personalInfo.first()
            val personalEntities = mutableListOf<PersonalInfoEntity>()

            val personalInfoFields = personalInfo.javaClass.declaredFields
            val personalInfoFieldNames = personalInfoFields.map { field -> field.name }

            for ((idx, field) in personalInfoFields.withIndex()) {
                field.isAccessible = true
                personalEntities += PersonalInfoEntity(
                    username,
                    personalInfoFieldNames[idx],
                    field.get(personalInfo)?.toString()?.trim() ?: ""
                )
            }

            personalInfoDao.insert(*personalEntities.toTypedArray())

            // Fetch tuition information and insert into table.
            val tuitionFees = studentInfoResponse.tuitionFees
            val tuitionEntities = mutableListOf<TuitionEntity>()

            for (tuition in tuitionFees) {
                tuitionEntities += TuitionEntity(
                    username,
                    tuition.paidDate,
                    tuition.tuitionAmount ?: 0,
                    tuition.enterAmount ?: 0,
                    tuition.year,
                    tuition.semester,
                    tuition.stateCode
                )
            }

            tuitionDao.insert(*tuitionEntities.toTypedArray())


            // Fetch scholarship information and insert into table.
            val scholarships = studentInfoResponse.scholarships
            val scholarshipEntities = mutableListOf<ScholarshipEntity>()

            for (scholarship in scholarships) {
                scholarshipEntities += ScholarshipEntity(
                    username,
                    scholarship.scholarshipName,
                    scholarship.scholarshipEnterAmount ?: 0,
                    scholarship.scholarshipTuitionAmount ?: 0,
                    scholarship.etcAmount ?: 0,
                    scholarship.year,
                    scholarship.semester,
                    scholarship.date
                )
            }

            scholarshipDao.insert(*scholarshipEntities.toTypedArray())
        } catch (e: Exception) {
            Log.e(MessageUtils.LOG_KEY, "${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(studentInfoResponse)
    }

    override suspend fun getPersonalInfo(): UseCase<List<PersonalInfoEntity>> {
        val username = preferenceManager.username
        val personalInfo: List<PersonalInfoEntity>

        try {
            personalInfo = personalInfoDao.getAll(username)
        } catch (e: Exception) {
            Log.e(MessageUtils.LOG_KEY, "${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(personalInfo)
    }

    override suspend fun getDeptTransferInfo(): UseCase<List<DeptTransferEntity>> {
        val username = preferenceManager.username
        val deptTransferInfo: List<DeptTransferEntity>

        try {
            deptTransferInfo = deptTransferDao.getAll(username)
        } catch (e: Exception) {
            Log.e(MessageUtils.LOG_KEY, "${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(deptTransferInfo)
    }

    override suspend fun getStudentStateChangeInfo(): UseCase<List<StudentStateChangeEntity>> {
        val username = preferenceManager.username
        val studentStateChangeEntity: List<StudentStateChangeEntity>

        try {
            studentStateChangeEntity = studentStateChangeDao.getAll(username)
        } catch (e: Exception) {
            Log.e(MessageUtils.LOG_KEY, "${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(studentStateChangeEntity)
    }

    override suspend fun getTuitionInfo(): UseCase<List<TuitionEntity>> {
        val username = preferenceManager.username
        val tuitionEntity: List<TuitionEntity>

        try {
            tuitionEntity = tuitionDao.getAll(username)
        } catch (e: Exception) {
            Log.e(MessageUtils.LOG_KEY, "${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(tuitionEntity)
    }

    override suspend fun getScholarshipInfo(): UseCase<List<ScholarshipEntity>> {
        val username = preferenceManager.username
        val scholarshipEntities: List<ScholarshipEntity>

        try {
            scholarshipEntities = scholarshipDao.getAll(username)
        } catch (e: Exception) {
            Log.e(MessageUtils.LOG_KEY, "${e.message}")
            return UseCase.error("${e.message}")
        }

        return UseCase.success(scholarshipEntities)
    }
}