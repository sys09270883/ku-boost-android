package com.konkuk.boost.data.course

import com.google.gson.annotations.SerializedName

data class SyllabusResponse(@SerializedName("DS_LECTUREINFO") val lectureInfoList: List<LectureInfo>)
