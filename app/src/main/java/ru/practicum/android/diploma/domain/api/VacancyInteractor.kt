package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.VacancyState

interface VacancyInteractor {
    suspend fun getVacancies(filteredQuery: Map<String, String>): VacancyState
}
