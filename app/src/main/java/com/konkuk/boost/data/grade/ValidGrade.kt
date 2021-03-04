package com.konkuk.boost.data.grade

import com.google.gson.annotations.SerializedName

data class ValidGrade(
    @SerializedName("SBJT_ID") val subjectId: String,
    @SerializedName("YY") val year: String,
    @SerializedName("SHTM") val semesterCode: String,
)
