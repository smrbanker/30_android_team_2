package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState

interface SearchVacancyDetailsInteractor {
    suspend fun searchVacancyDetails(id: String): Resource<Vacancy>
}
