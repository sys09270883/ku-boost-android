package com.corgaxm.ku_alarmy.data.crawl

import com.google.gson.annotations.SerializedName

data class GraduationSimulationResponse(
    @SerializedName("responseCode") val responseCode: Int,
    @SerializedName("graduationSimulation") val graduationSimulation: GraduationSimulation
)
