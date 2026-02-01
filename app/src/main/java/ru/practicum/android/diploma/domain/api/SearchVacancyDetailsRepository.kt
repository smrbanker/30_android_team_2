package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

interface SearchVacancyDetailsRepository {
    fun searchVacancyDetails(id: String): Flow<Resource<Vacancy>>
}

