package com.konkuk.boost.data.course

import com.google.gson.annotations.SerializedName

data class SyllabusLecturePlan(
    @SerializedName("SBJT_ID") val subjectId: String,           // 과목번호
    @SerializedName("KOR_NM") val professor: String,            // 교수명
    @SerializedName("CORS_KOR_NM") val courseName: String,      // 과목명
    @SerializedName("LT_PURP") val lecturePurpose: String,      // 목표
    @SerializedName("EMAIL") val email: String,                 // 이메일
    @SerializedName("OFCE_TEL_NO") val tellNo: String,          // 연락처
    @SerializedName("CNSL_POSB_TM") val consultingTime: String, // 상담가능시간
    @SerializedName("E_RATE") val eRate: String,                // 수업비중
    @SerializedName("LECTURE_NOTE") val lectureNote: String,    // 참고사항
)
