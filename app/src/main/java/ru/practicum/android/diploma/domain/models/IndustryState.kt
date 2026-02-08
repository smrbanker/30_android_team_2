package ru.practicum.android.diploma.domain.models

sealed class IndustryState {
    data object Loading : IndustryState()
    data class Content(val industries: List<Industry>) : IndustryState()
    data object Empty : IndustryState()
    data class Error(val message: String) : IndustryState()
}
