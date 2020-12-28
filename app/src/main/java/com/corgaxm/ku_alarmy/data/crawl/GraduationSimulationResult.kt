package com.corgaxm.ku_alarmy.data.crawl

import com.google.gson.annotations.SerializedName

data class GraduationSimulationResult(
    @SerializedName("basic_elective") val basicElective: Int?,
    @SerializedName("advanced_elective") val advancedElective: Int?,
    @SerializedName("general_elective") val generalElective: Int?,
    @SerializedName("core_elective") val coreElective: Int?,
    @SerializedName("normal_elective") val normalElective: Int?,
    @SerializedName("general_requirement") val generalRequirement: Int?,
    @SerializedName("major_requirement") val majorRequirement: Int?,
    @SerializedName("major_elective") val majorElective: Int?,
    @SerializedName("general_selective") val generalSelective: Int?,
    @SerializedName("dual_elective") val dualElective: Int?,
    @SerializedName("dual_requirement") val dualRequirement: Int?,
    @SerializedName("dual_major_elective") val dualMajorElective: Int?,
    @SerializedName("etc") val etc: Int?,
    @SerializedName("total") val total: Int?
)

