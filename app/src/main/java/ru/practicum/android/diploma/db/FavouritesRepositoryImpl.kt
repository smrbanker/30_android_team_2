package ru.practicum.android.diploma.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.domain.db.FavouritesRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.VacancyDbConverter

class FavouriteRepositoryImpl(
    private val database: Database,
    private val vacancyDbConverter: VacancyDbConverter
) : FavouritesRepository {

    override suspend fun insertNewFavoriteVacancy(favoriteVacancy: Vacancy) {
        database.FavoritesDao().insertNewFavoriteVacancy(vacancyDbConverter.map(favoriteVacancy))
    }

    override suspend fun deleteFavoriteVacancy(favoriteVacancy: Vacancy) {
        database.FavoritesDao().deleteFavoriteVacancy(vacancyDbConverter.map(favoriteVacancy))
    }

    override fun getAllFavoriteVacancy(): Flow<List<Vacancy>> = flow {
        val vacancies = database.FavoritesDao().getAllFavoriteVacancy()
        emit(convertFromVacancyDetailEntity(vacancies))
    }

    override suspend fun getFavoriteVacancy(id: String): Vacancy {
        val vacancy = database.FavoritesDao().getFavoriteVacancy(id)
        return vacancyDbConverter.map(vacancy)
    }

    private fun convertFromVacancyDetailEntity(vacancies: List<VacancyDetailEntity>): List<Vacancy> {
        return vacancies.map { vacancy -> vacancyDbConverter.map(vacancy) }
    }
}
