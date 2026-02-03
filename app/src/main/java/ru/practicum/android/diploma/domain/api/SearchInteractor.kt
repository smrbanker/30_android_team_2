package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.SearchResult

interface SearchInteractor {
    suspend fun searchVacancies(options: Map<String, String>): Resource<SearchResult>
}

