package ru.practicum.android.diploma.domain.models

sealed interface VacancyState {
    data object Loading : VacancyState
    data object Empty : VacancyState
    data class Content(val vacanciesList: List<Vacancy>) : VacancyState
    data class Error(val errorMessage: String) : VacancyState
}
