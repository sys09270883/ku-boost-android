package com.corgaxm.ku_alarmy.data.grade

import com.google.gson.annotations.SerializedName

data class GradeResponse(
    @SerializedName("DS_GRADEOFSTUDENT") val grades: List<Grade>
)
