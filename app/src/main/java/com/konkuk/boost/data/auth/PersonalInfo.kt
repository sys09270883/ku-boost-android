package com.konkuk.boost.data.auth

import com.google.gson.annotations.SerializedName

data class PersonalInfo(
    @SerializedName("ZIP") val zipCode: String?,
    @SerializedName("HAND_NO") val cellPhoneNo: String?,
    @SerializedName("ENTR_DT") val enterDate: String?,
    @SerializedName("CHA_NM") val chineseName: String?,
    @SerializedName("SHYR") val schoolYear: String,
    @SerializedName("EMAIL") val email: String?,
    @SerializedName("HSCH_NM") val highSchoolName: String?,
    @SerializedName("UNIV_NM") val universityName: String?,
    @SerializedName("GEN") val gender: String?,
    @SerializedName("NATV_HSCH_GRDT_DT") val highSchoolGraduationDate: String?,
    @SerializedName("EALY_GRDT_CHECK") val earlyGraduationAvailability: String?,
    @SerializedName("COUN_CD") val country: String?,
    @SerializedName("TEL_NO") val tellNo: String?,
    @SerializedName("KOR_NM") val koreanName: String?,
    @SerializedName("ENG_NM") val englishName: String?,
    @SerializedName("BIRTH_DT") val birthday: String?,
    @SerializedName("STD_DIV") val studentDiv: String?,
    @SerializedName("ENTR_CD") val enterCode: String?,
    @SerializedName("ADDR") val address: String?,
    @SerializedName("HANDICAP_FG") val impairment: String?,
)
