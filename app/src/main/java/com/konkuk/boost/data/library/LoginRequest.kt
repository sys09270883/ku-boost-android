package com.konkuk.boost.data.library

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("loginId") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("isMobile") val isMobile: Boolean = true
)