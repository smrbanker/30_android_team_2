package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancySearchResult

class SearchVacanciesInteractorImpl(
    private val repository: VacancyRepository
) : SearchVacanciesInteractor {

    override suspend fun searchVacancies(
        query: String,
        page: Int,
        perPage: Int
    ): Result<VacancySearchResult> {
        return repository.searchVacancies(query, page, perPage)
    }
}

