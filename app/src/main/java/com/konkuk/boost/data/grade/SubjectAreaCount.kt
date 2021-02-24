package com.konkuk.boost.data.grade

import com.konkuk.boost.persistence.area.SubjectAreaEntity

data class SubjectAreaCount(
    val area: SubjectAreaEntity,
    var count: Int = 0
)
