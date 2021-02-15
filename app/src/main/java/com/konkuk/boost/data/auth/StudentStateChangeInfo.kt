package com.konkuk.boost.data.auth

import com.google.gson.annotations.SerializedName

data class StudentStateChangeInfo(
    @SerializedName("APLY_DT") val applyDate: String,   // 신청일자
    @SerializedName("CHG_DT") val changedDate: String,  // 학적변동일자
    @SerializedName("CHG_CD") val changedCode: String,  // 학적변동구분
    @SerializedName("CHG_RESN_CD") val changedReason: String?,  // 학적변동사유
    @SerializedName("APLY_ST_CD") val appliedStateCode: String, // 처리상태
)
