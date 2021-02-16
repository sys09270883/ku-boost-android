package com.konkuk.boost.data.auth

import com.google.gson.annotations.SerializedName

data class Tuition(
    @SerializedName("PAID_DT") val paidDate: String,    // 납부일자
    @SerializedName("LSN_AMT") val tuitionAmount: Int?, // 수업료
    @SerializedName("ENTR_AMT") val enterAmount: Int?,  // 입학금
    @SerializedName("REG_YY") val year: Int,            // 년도
    @SerializedName("REG_SHTM_NM") val semester: String,    // 학기
    @SerializedName("REG_ST_CD") val stateCode: String,     // 납부여부
)
