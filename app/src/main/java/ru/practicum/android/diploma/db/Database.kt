package ru.practicum.android.diploma.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.db.dao.FavoritesDao
import ru.practicum.android.diploma.db.entity.VacancyDetailEntity

@Database(
    version = 1,
    entities = [
        VacancyDetailEntity::class
    ])
abstract class Database : RoomDatabase() {
    abstract fun FavoritesDao(): FavoritesDao
}
