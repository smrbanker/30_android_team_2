package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.db.FavouritesRepositoryImpl
import ru.practicum.android.diploma.domain.db.FavouritesRepository
import com.google.gson.Gson
import ru.practicum.android.diploma.data.FilterSpRepositoryImpl
import ru.practicum.android.diploma.data.repository.SearchVacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.util.VacancyDbConverter
import ru.practicum.android.diploma.data.repository.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.api.FilterSpRepository
import ru.practicum.android.diploma.domain.api.SearchVacancyDetailsRepository
import ru.practicum.android.diploma.domain.api.VacancyRepository

val repositoryModule = module {
    factory<VacancyRepository> {
        VacancyRepositoryImpl(get())
    }

    factory {
        Gson()
    }

    factory<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    factory { VacancyDbConverter() }

    single<SearchVacancyDetailsRepository> {
        SearchVacancyDetailsRepositoryImpl(get())
    }

    factory<FilterSpRepository> {
        FilterSpRepositoryImpl(get(), get())
    }
}
