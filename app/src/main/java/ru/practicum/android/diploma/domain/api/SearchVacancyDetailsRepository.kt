package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

interface SearchVacancyDetailsRepository {
    suspend fun searchVacancyDetails(id: String): Resource<Vacancy>
}
