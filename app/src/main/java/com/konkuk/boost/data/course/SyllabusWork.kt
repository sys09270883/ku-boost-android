package com.konkuk.boost.data.course

import com.google.gson.annotations.SerializedName

data class SyllabusWork(
    @SerializedName("HMWK_NM") val homeworkName: String,
    // B51001: 과제, B51002: 퀴즈, B51003: 발표, B51004:프로젝트, B51005: 토론
    @SerializedName("GRAD_DIV") val homeworkDiv: String?,
)
