package com.konkuk.boost.data.auth

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("dmRes") val response: ChangePasswordFlag
)
