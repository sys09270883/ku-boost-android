package com.konkuk.boost.data.course

import com.google.gson.annotations.SerializedName

data class SyllabusWork(
    @SerializedName("HMWK_NM") val homeworkName: String,
)
