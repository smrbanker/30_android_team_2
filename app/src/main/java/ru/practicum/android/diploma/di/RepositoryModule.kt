package ru.practicum.android.diploma.di

import com.google.gson.Gson
import org.koin.dsl.module
import ru.practicum.android.diploma.data.FilterSpRepositoryImpl
import ru.practicum.android.diploma.data.repository.AreasRepositoryImpl
import ru.practicum.android.diploma.data.repository.SearchVacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.data.repository.VacancyRepositoryImpl
import ru.practicum.android.diploma.db.FavouritesRepositoryImpl
import ru.practicum.android.diploma.domain.api.AreasRepository
import ru.practicum.android.diploma.domain.api.FilterSpRepository
import ru.practicum.android.diploma.domain.api.SearchVacancyDetailsRepository
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.db.FavouritesRepository
import ru.practicum.android.diploma.util.VacancyDbConverter

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

    factory<AreasRepository> {
        AreasRepositoryImpl(get())
    }
}
