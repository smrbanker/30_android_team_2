package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.VacancySearchResult

interface SearchVacanciesInteractor {
    suspend fun searchVacancies(
        query: String,
        page: Int,
        perPage: Int = 20
    ): Result<VacancySearchResult>
}

