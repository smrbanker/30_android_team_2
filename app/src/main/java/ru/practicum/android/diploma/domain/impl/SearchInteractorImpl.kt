package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.SearchResult

class SearchInteractorImpl(
    private val repository: SearchRepository
) : SearchInteractor {

    override suspend fun searchVacancies(options: Map<String, String>): Resource<SearchResult> {
        return repository.searchVacancies(options)
    }
}

