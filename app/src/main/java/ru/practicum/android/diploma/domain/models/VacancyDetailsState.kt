package ru.practicum.android.diploma.domain.models

sealed interface VacancyDetailsState {
    object Loading : VacancyDetailsState

    data class Content(
        val vacancy: List<Vacancy>
    ) : VacancyDetailsState

    data class Error(
        val errorMessage: String
    ) : VacancyDetailsState

    data class Empty(
        val emptyMessage: String
    ) : VacancyDetailsState
}

