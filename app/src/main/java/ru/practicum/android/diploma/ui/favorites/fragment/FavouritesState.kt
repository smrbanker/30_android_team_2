package ru.practicum.android.diploma.ui.favorites.fragment

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface FavouritesState {

    data class Content(
        val vacancies: List<Vacancy>
    ) : FavouritesState

    data object Empty : FavouritesState
    data object Error : FavouritesState
    data object Loading : FavouritesState
}
