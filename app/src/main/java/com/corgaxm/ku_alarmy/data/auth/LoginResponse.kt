package com.corgaxm.ku_alarmy.data.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName(value = "_METADATA_") val loginSuccess: LoginSuccess?,
    @SerializedName(value = "ERRMSGINFO") val loginFailure: LoginFailure?
)
