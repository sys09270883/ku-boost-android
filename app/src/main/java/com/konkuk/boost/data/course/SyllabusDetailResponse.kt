package com.konkuk.boost.data.course

import com.google.gson.annotations.SerializedName

data class SyllabusDetailResponse(
    @SerializedName("DS_WEEKPLAN") val weekPlan: List<SyllabusPlan>,
    // TODO

)
