package com.konkuk.boost.data.auth

import com.google.gson.annotations.SerializedName

data class Scholarship(
    @SerializedName("YY") val year: String, // 년도
    @SerializedName("SHTM_NM") val semester: String,    // 학기
    @SerializedName("SCAL_KOR_NM") val scholarshipName: String, // 장학금명
    @SerializedName("SCAL_ENTR_AMT") val scholarshipEnterAmount: Int, // 장학입학금
    @SerializedName("SCAL_LSN_AMT") val scholarshipTuitionAmount: Int, // 장학수업료
    @SerializedName("ETC_SCAL_AMT") val etcAmount: Int, // 장학입학금
    @SerializedName("BNFT_DT") val date: String, // 지급일자
)
