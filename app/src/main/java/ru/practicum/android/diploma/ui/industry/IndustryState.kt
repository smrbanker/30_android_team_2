package ru.practicum.android.diploma.ui.industry

import ru.practicum.android.diploma.domain.models.Sector

sealed interface IndustryState {

    data class Content(
        val industries: List<Sector>,
        val flag: Boolean
    ) : IndustryState

    data class Error(
        val message: String
    ) : IndustryState

    data object Empty : IndustryState
    data object Loading : IndustryState
}
