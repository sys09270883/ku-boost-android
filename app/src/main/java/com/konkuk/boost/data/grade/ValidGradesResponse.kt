package com.konkuk.boost.data.grade

import com.google.gson.annotations.SerializedName

data class ValidGradesResponse(
    @SerializedName("DS_UPPER") val validGrades: List<ValidGrade>
)