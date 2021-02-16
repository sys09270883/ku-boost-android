package com.konkuk.boost.data.auth

import com.google.gson.annotations.SerializedName

data class DeptTransferInfo(
    @SerializedName("CHG_DT") val changedDate: String,  // 변경일자
    @SerializedName("BF_COLG") val beforeDept: String?,  // 변경 전 대학
    @SerializedName("BF_SUST") val beforeSust: String?,  // 변경 전 학부
    @SerializedName("BF_MJ") val beforeMajor: String?,  // 변경 전 전공
    @SerializedName("CHG_FG_CD") val changedCode: String,  // 변경구분
    @SerializedName("CHG_YY") val changedYear: String,  // 변경년도
    @SerializedName("CHG_SHTM") val changedSemester: String,  // 변경학기
    @SerializedName("CHG_COLG") val dept: String?,  // 변경 후 대학
    @SerializedName("CHG_SUST") val sust: String?,  // 변경 후 학부
    @SerializedName("CHG_MJ") val major: String?,  // 변경 후 전공
)
