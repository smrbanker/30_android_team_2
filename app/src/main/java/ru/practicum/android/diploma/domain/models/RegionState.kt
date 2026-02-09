package ru.practicum.android.diploma.domain.models

interface RegionState {
    object Loading : RegionState

    data class Content(
        val regionList: List<Region>
    ) : RegionState

    data class Error(
        val errorMessage: String
    ) : RegionState

    object Empty : RegionState
}
