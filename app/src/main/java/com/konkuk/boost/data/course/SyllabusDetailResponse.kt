package com.konkuk.boost.data.course

import com.google.gson.annotations.SerializedName

data class SyllabusDetailResponse(
    @SerializedName("DS_WEEKPLAN") val weekPlan: List<SyllabusWeekPlan>,
    @SerializedName("DS_COURLECTUREPLAN") val lecturePlan: List<SyllabusLecturePlan>,
    @SerializedName("DS_BOOK") val book: List<SyllabusBook>,
    @SerializedName("DS_WORK") val work: List<SyllabusWork>,
)
