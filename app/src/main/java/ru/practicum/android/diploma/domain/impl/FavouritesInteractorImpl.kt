package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.db.FavouritesInteractor
import ru.practicum.android.diploma.domain.db.FavouritesRepository
import ru.practicum.android.diploma.domain.models.Vacancy

class FavouritesInteractorImpl(
    private val favouritesRepository: FavouritesRepository
) : FavouritesInteractor {

    override suspend fun insertNewFavoriteVacancy(favoriteVacancy: Vacancy) {
        favouritesRepository.insertNewFavoriteVacancy(favoriteVacancy)
    }

    override suspend fun deleteFavoriteVacancy(favoriteVacancy: Vacancy) {
        favouritesRepository.deleteFavoriteVacancy(favoriteVacancy)
    }

    override fun getAllFavoriteVacancy(): Flow<List<Vacancy>> {
        return favouritesRepository.getAllFavoriteVacancy()
    }

    override suspend fun getFavoriteVacancy(id: String): Vacancy? {
        return favouritesRepository.getFavoriteVacancy(id)
    }
}
