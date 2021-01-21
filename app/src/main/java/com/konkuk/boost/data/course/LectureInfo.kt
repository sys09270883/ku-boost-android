package com.konkuk.boost.data.course

import com.google.gson.annotations.SerializedName
import java.util.*

data class LectureInfo(
    @SerializedName("SBJT_ID") val subjectId: String,           // 과목번호
    @SerializedName("CORS_NM") val subjectName: String,         // 과목명
    @SerializedName("HAKSU_ID") val subjectNumber: String,      // 학수번호
    @SerializedName("LTPROF") val professor: String?,           // 교수명
    @SerializedName("LTROOM") val lectureRoom: String,          // 강의실
    @SerializedName("LT_DAY_TIME") val lectureDayTime: String,  // 강의시간
    @SerializedName("POBT_DIV_NM") val classification: String,  // 이수구분
    @SerializedName("OPEN_SUST_NM") val dept: String,           // 학과
    @SerializedName("PNT") val subjectPoint: Int,               // 학점
) {
    override fun equals(other: Any?): Boolean {
        (other as? LectureInfo)?.let {
            return it.subjectId == other.subjectId
        } ?: return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(this.subjectId)
    }
}

