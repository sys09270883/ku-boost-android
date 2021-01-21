package com.konkuk.boost.data.course

import com.google.gson.annotations.SerializedName

data class SyllabusWeekPlan(
    @SerializedName("WEEK_NO") val weekNo: Int,
    @SerializedName("LT_CTNT") val lectureContent: String,
    @SerializedName("THEME") val theme: String,
    @SerializedName("WEK_ROOM_NM") val roomName: String,
)
