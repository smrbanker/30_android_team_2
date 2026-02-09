package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun doAreasRequest(): Response
    suspend fun doIndustryRequest(): Response
    suspend fun doSearchRequest(options: Map<String, String>): Response
    suspend fun doVacancyRequest(id: String): Response
}
