package com.konkuk.boost.data.grade

import com.google.gson.annotations.SerializedName

data class GraduationSimulationResponse(
    @SerializedName("DS_SIMUL1") val simulations: List<GraduationSimulation>,
)
