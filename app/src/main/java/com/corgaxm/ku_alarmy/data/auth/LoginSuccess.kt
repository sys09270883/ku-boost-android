package com.corgaxm.ku_alarmy.data.auth

import com.google.gson.annotations.SerializedName

data class LoginSuccess(@SerializedName(value = "success") val isSucceeded: Boolean)
