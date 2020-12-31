package com.corgaxm.ku_alarmy.data.grade

import com.google.gson.annotations.SerializedName

data class Grade(
    @SerializedName("APPR_MTHD_CD") val evaluationMethod: String,   // 성적평가방식
    @SerializedName("COMM_NM") val semester: String,                // 학기
    @SerializedName("POBT_NM") val classification: String,          // 이수구분
    @SerializedName("GRD") val characterGrade: String,              // 알파벳 성적
    @SerializedName("MRKS") val grade: Float,                       // 성적
    @SerializedName("KOR_NM") val professor: String,                // 담당교수
    @SerializedName("TYPL_KOR_NM") val subjectName: String,         // 과목명
    @SerializedName("HAKSU_ID") val subjectNumber: String,          // 학수번호
    @SerializedName("SBJT_ID") val subjectId: String,               // 과목 ID
    @SerializedName("PNT") val subjectPoint: Int,                // 학점
)
