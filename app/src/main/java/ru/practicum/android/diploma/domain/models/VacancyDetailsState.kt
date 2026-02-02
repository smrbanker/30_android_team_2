package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.ui.vacancy.fragment.VacancyCastItem

sealed interface VacancyDetailsState {
    object Loading : VacancyDetailsState

    data class Content(
        val vacancy: List<VacancyCastItem>
    ) : VacancyDetailsState

    data class Error(
        val errorMessage: String
    ) : VacancyDetailsState

    data class Empty(
        val emptyMessage: String
    ) : VacancyDetailsState
}

