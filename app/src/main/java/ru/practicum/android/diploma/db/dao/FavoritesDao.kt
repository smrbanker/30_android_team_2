package ru.practicum.android.diploma.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.db.entity.VacancyDetailEntity

@Dao
interface FavoritesDao {
    @Insert(entity = VacancyDetailEntity::class, onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertNewFavoriteVacancy(favoriteVacancy: VacancyDetailEntity)

    @Update(entity = VacancyDetailEntity::class, onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun updateFavoriteVacancy(favoriteVacancy: VacancyDetailEntity)

    @Delete(entity = VacancyDetailEntity::class)
    suspend fun deleteFavoriteVacancy(favoriteVacancy: VacancyDetailEntity)

    @Query("SELECT * FROM favorites_vacancies_table ORDER BY insert_time DESC")
    fun getAllFavoriteVacancy(): Flow<List<VacancyDetailEntity>>
}
