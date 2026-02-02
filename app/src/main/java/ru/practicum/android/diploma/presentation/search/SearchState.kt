package ru.practicum.android.diploma.presentation.search

import ru.practicum.android.diploma.domain.models.Vacancy

sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class Content(val vacancies: List<Vacancy>, val found: Int) : SearchState()
    data class Error(val message: String) : SearchState()
    object Empty : SearchState()
}

