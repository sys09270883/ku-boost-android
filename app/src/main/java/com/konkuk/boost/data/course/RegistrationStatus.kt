package com.konkuk.boost.data.course

data class RegistrationStatus(
    val classBasketNumber: String = "0",    // 수강바구니(자동신청) 담은 인원
    val registrationNumber: String = "0",   // 수강인원
    val limitedNumber: String = "0",        // 제한인원
)
