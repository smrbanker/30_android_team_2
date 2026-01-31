package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.db.FavouritesRepositoryImpl
import ru.practicum.android.diploma.domain.db.FavouritesRepository
import com.google.gson.Gson
import ru.practicum.android.diploma.util.VacancyDbConverter
import ru.practicum.android.diploma.data.repository.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.api.VacancyRepository

val repositoryModule = module {
    factory<VacancyRepository> {
        VacancyRepositoryImpl(get())
    }

    factory {
        Gson()
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    factory { VacancyDbConverter() }
}
