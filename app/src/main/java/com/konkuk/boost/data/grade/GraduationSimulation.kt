package com.konkuk.boost.data.grade

import com.google.gson.annotations.SerializedName

data class GraduationSimulation(
    @SerializedName("GRDT_COND_NM") val classification: String, // 이수구분
    @SerializedName("MOD_PNT") val remainder: Int?,             // 잔여학점
    @SerializedName("SUM_PNT") val acquired: String?,           // 취득학점, String 타입 확인할 것
    @SerializedName("POBT_PNT") val standard: Int?,             // 기준학점
)
