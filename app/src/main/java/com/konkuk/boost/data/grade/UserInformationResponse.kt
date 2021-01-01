package com.konkuk.boost.data.grade

import com.google.gson.annotations.SerializedName

data class UserInformationResponse(
    @SerializedName("dmUserInfo") val userInformation: UserInformation
)
