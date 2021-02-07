package com.konkuk.boost.data.grade

import com.google.gson.annotations.SerializedName

data class UserInformation(
    @SerializedName("USER_ID") val stdNo: String,   // ex) 201511271
    @SerializedName("SHREG_NM") val state: String?, // ex) 재학생
    @SerializedName("DEPT_TTNM") val dept: String?, // ex) 공과대학 컴퓨터공학부
    @SerializedName("USER_NM") val name: String?,   // ex) 신윤섭
    @SerializedName("SHREG_CD") val code: String,   // ex) A08001
)
