package com.corgaxm.ku_alarmy.data.crawl

import com.google.gson.annotations.SerializedName

data class GraduationSimulation(
    @SerializedName(value = "standard") val standard: GraduationSimulationResult,
    @SerializedName(value = "acquired") val acquired: GraduationSimulationResult
)
