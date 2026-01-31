package ru.practicum.android.diploma.di

import com.google.gson.Gson
import org.koin.dsl.module
import ru.practicum.android.diploma.db.FavouritesRepositoryImpl
import ru.practicum.android.diploma.domain.db.FavouritesRepository
import ru.practicum.android.diploma.util.VacancyDbConverter
import ru.practicum.android.diploma.data.dto.models.VacancyConverter
import ru.practicum.android.diploma.data.repository.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.api.VacancyRepository

val repositoryModule = module {
    factory<VacancyRepository> {
        VacancyRepositoryImpl(get(), get())
    }

    factory {
        Gson()
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    factory { VacancyDbConverter() }

    factory {
        VacancyConverter(get())
    }
}
