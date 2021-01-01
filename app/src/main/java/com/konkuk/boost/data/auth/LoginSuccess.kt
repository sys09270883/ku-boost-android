package com.konkuk.boost.data.auth

import com.google.gson.annotations.SerializedName

data class LoginSuccess(@SerializedName(value = "success") val isSucceeded: Boolean)
