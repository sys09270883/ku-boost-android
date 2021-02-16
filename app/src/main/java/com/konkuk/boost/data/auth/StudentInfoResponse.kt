package com.konkuk.boost.data.auth

import com.google.gson.annotations.SerializedName

data class StudentInfoResponse(
    @SerializedName("DS_TUIT100") val tuitionFees: List<Tuition>,   // 등록금
    @SerializedName("DS_REGI110") val personalInfo: List<PersonalInfo>, // 인적사항
    @SerializedName("DS_REGI310") val studentStateChangeInfo: List<StudentStateChangeInfo>, // 학적변동
    @SerializedName("DS_REGI410") val deptTransferInfo: List<DeptTransferInfo>,    // 학과변경정보
    @SerializedName("DS_SCHO001") val scholarships: List<Scholarship>,  // 장학정보
    @SerializedName("DS_WARNHONOR") val warnHonors: List<WarnHonor>,    // 포상징계
    @SerializedName("dmPhoto") val profilePhoto: ProfilePhoto,    // 프로필 사진 (base 64 디코딩)
)
