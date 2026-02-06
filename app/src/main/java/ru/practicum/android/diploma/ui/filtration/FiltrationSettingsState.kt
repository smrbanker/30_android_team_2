package ru.practicum.android.diploma.ui.filtration

import ru.practicum.android.diploma.domain.models.Filter

sealed interface FiltrationSettingsState {

    data class Content(
        val filter: Filter
    ) : FiltrationSettingsState

    data class Error(
        val message: String
    ) : FiltrationSettingsState
}
