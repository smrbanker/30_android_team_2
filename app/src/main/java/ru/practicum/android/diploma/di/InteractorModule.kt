package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.FilterSpInteractor
import ru.practicum.android.diploma.domain.api.IndustryInteractor
import ru.practicum.android.diploma.domain.api.SearchVacancyDetailsInteractor
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.db.FavouritesInteractor
import ru.practicum.android.diploma.domain.impl.FavouritesInteractorImpl
import ru.practicum.android.diploma.domain.impl.FilterSpInteractorImpl
import ru.practicum.android.diploma.domain.impl.IndustryInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchVacancyDetailsInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl

val interactorModule = module {

    factory<FavouritesInteractor> {
        FavouritesInteractorImpl(get())
    }
    factory<VacancyInteractor> {
        VacancyInteractorImpl(get())
    }

    factory<SearchVacancyDetailsInteractor> {
        SearchVacancyDetailsInteractorImpl(get())
    }

    factory<FilterSpInteractor> {
        FilterSpInteractorImpl(get())
    }

    factory<IndustryInteractor> {
        IndustryInteractorImpl(get())
    }
}
