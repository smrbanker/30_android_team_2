package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.ui.vacancy.fragment.VacancyCastItem

sealed interface VacancyDetailsState {
    object Loading: VacancyDetailsState

    data class Content(
        val vacancy: List<VacancyCastItem>,
        val vacancyFull: Vacancy?
    ) : VacancyDetailsState

    data class Error(
        val errorMessage: String
    ) : VacancyDetailsState

    data class Empty(
        val emptyMessage: String
    ) : VacancyDetailsState

    data class ErrorDB(
        val errorMessageDB: String
    ) : VacancyDetailsState
}
