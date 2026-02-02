package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.VacancySearchResponseDto

interface ApiService {
    @GET("vacancies")
    suspend fun searchVacancies(
        @HeaderMap headers: Map<String, String>,
        @QueryMap params: Map<String, String>
    ): VacancySearchResponseDto
}

