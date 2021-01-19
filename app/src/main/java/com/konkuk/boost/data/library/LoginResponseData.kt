package com.konkuk.boost.data.library

import com.google.gson.annotations.SerializedName

data class LoginResponseData(@SerializedName("accessToken") val accessToken: String)
