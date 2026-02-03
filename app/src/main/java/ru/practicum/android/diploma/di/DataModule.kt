package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.db.Database

val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), Database::class.java, "database.db")
            .build()
    }
    single { get<Database>().FavoritesDao() }
}
