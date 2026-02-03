package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.domain.models.Vacancy

sealed class SearchState {
    object Default : SearchState()
    object Loading : SearchState()
    data class Content(
        val vacancies: List<Vacancy>,
        val found: Int,
        val isLoadingMore: Boolean = false
    ) : SearchState()
    data class Error(val message: String) : SearchState()
    object Empty : SearchState()
}

