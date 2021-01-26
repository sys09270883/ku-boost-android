package com.konkuk.boost.data.course

import com.konkuk.boost.persistence.LikeCourseEntity

data class RegistrationStatusData(
    val likeCourseEntity: LikeCourseEntity,
    val registrationStatusList: List<RegistrationStatus>
)
