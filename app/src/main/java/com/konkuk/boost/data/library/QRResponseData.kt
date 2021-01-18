package com.konkuk.boost.data.library

import com.google.gson.annotations.SerializedName

data class QRResponseData(
    @SerializedName("thumbnail") val thumbnailUrl: String,
    @SerializedName("membershipCard") val membershipCard: String,
)
