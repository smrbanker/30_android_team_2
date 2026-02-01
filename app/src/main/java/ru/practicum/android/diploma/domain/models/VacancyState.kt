package ru.practicum.android.diploma.domain.models

sealed interface VacancyState {
    data object Loading : VacancyState
    data object Empty : VacancyState
    data class Content(val vacanciesList: List<Vacancy>, val itemsFound: Int) : VacancyState
    data class Error(val errorMessage: String) : VacancyState
}
