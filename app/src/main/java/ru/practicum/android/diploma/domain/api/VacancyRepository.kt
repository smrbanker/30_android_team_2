package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacancyRepository {
    suspend fun getVacancies(filteredQuery: Map<String, String>): Resource<List<Vacancy>>
}
