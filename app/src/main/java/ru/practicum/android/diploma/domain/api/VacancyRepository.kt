package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyResource

interface VacancyRepository {
    suspend fun getVacancies(filteredQuery: Map<String, String>): Resource<VacancyResource>
}
