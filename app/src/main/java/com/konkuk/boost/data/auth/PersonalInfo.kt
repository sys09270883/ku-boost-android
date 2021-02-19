package com.konkuk.boost.data.auth

import com.google.gson.annotations.SerializedName

data class PersonalInfo(
    @SerializedName("ZIP") val zipCode: String?, // 우편번호
    @SerializedName("HAND_NO") val cellPhoneNo: String?, // 핸드폰번호
    @SerializedName("ENTR_DT") val enterDate: String?,   // 입학일자 (yyyy/MM/dd)
    @SerializedName("CHA_NM") val chineseName: String?,  // 한자이름
    @SerializedName("SHYR") val schoolYear: String, // 학년
    @SerializedName("EMAIL") val email: String?, // 이메일
    @SerializedName("HSCH_NM") val highSchoolName: String?, // 출신 고등학교 이름
    @SerializedName("UNIV_NM") val universityName: String?, // 출신 대학교 이름
    @SerializedName("GEN") val gender: String?, // 성별
    @SerializedName("NATV_HSCH_GRDT_DT") val highSchoolGraduationDate: String?, // 고등학교 졸업일자
    @SerializedName("EALY_GRDT_CHECK") val earlyGraduationAvailability: String?, // 조기졸업여부
    @SerializedName("COUN_CD") val country: String?, // 국적
    @SerializedName("TEL_NO") val tellNo: String?, // 집 전화번호
    @SerializedName("KOR_NM") val koreanName: String?, // 한글이름
    @SerializedName("ENG_NM") val englishName: String?, // 영어이름
    @SerializedName("BIRTH_DT") val birthday: String?, // 생일 (yyMMdd)
    @SerializedName("STD_DIV") val studentDiv: String?, // 학생구분
    @SerializedName("ENTR_CD") val enterCode: String?, // 입학구분
    @SerializedName("ADDR") val address: String?, // 주소
    @SerializedName("HANDICAP_FG") val impairment: String?, // 장애여부
)
