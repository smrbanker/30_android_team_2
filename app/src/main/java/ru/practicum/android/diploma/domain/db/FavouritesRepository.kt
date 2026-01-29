package ru.practicum.android.diploma.domain.db

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavouritesRepository {
    suspend fun insertNewFavoriteVacancy(favoriteVacancy: Vacancy)
    suspend fun deleteFavoriteVacancy(favoriteVacancy: Vacancy)
    fun getAllFavoriteVacancy(): Flow<List<Vacancy>>
    suspend fun getFavoriteVacancy(id: String): Vacancy
}
