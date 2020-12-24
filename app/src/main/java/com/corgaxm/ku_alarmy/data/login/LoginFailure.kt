package com.corgaxm.ku_alarmy.data.login

import com.google.gson.annotations.SerializedName

data class LoginFailure(
    @SerializedName(value = "ERRMSG") val errorMessage: String,
    @SerializedName(value = "STATUSCODE") val statusCode: Int
)
