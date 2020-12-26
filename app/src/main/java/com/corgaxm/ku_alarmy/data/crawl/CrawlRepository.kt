package com.corgaxm.ku_alarmy.data.crawl

import com.corgaxm.ku_alarmy.utils.Resource

interface CrawlRepository {
    suspend fun makeGraduationSimulationRequest(): Resource<GraduationSimulationResponse>
}