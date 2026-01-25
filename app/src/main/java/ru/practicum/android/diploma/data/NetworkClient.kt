package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun doCountryRequest(): Response
    suspend fun doIndustryRequest(): Response
    suspend fun doRegionRequest(dto: Any): Response
    suspend fun doSearchRequest(dto: Any): Response
    suspend fun doVacancyRequest(dto: Any): Response
}
