package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.db.FavouritesRepositoryImpl
import ru.practicum.android.diploma.domain.db.FavouritesRepository
import ru.practicum.android.diploma.util.VacancyDbConverter

val repositoryModule = module {

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    factory { VacancyDbConverter() }
}
