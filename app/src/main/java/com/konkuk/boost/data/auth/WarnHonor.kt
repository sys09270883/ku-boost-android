package com.konkuk.boost.data.auth

import com.google.gson.annotations.SerializedName

data class WarnHonor(
    @SerializedName("LT_YY") val year: String,
    @SerializedName("LT_SHTM") val semesterCode: String,
    @SerializedName("HONOR_WARN_FG") val warnHonorFlag: String,
)
