package com.corgaxm.ku_alarmy.data.crawl

import com.google.gson.annotations.SerializedName

data class GraduationSimulationResult(
    @SerializedName("기교") val basicElective: Int?,
    @SerializedName("심교") val advancedElective: Int?,
    @SerializedName("지교") val generalElective: Int?,
    @SerializedName("일교") val normalElective: Int?,
    @SerializedName("핵교") val coreElective: Int?,
    @SerializedName("지필") val generalRequirement: Int?,
    @SerializedName("전필") val majorRequirement: Int?,
    @SerializedName("전선") val majorElective: Int?,
    @SerializedName("일선") val generalSelective: Int?,
    @SerializedName("다지") val dualElective: Int?,
    @SerializedName("다필") val dualRequirement: Int?,
    @SerializedName("다선") val dualMajorElective: Int?,
    @SerializedName("기타") val etc: Int?
)

