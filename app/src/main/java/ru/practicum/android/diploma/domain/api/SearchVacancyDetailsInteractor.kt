package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface SearchVacancyDetailsInteractor {
    fun searchVacancyDetails(id: String): Flow<Pair<Vacancy?, String?>>
}
