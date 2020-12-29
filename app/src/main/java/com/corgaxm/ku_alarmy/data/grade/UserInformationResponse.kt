package com.corgaxm.ku_alarmy.data.grade

import com.google.gson.annotations.SerializedName

data class UserInformationResponse(
    @SerializedName("dmUserInfo") val userInformation: UserInformation
)
