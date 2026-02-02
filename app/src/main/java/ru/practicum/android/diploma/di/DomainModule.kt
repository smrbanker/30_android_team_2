package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.impl.SearchVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacancyRepositoryImpl

val domainModule = module {
    single<VacancyRepository> { VacancyRepositoryImpl(get()) }
    single<SearchVacanciesInteractor> { SearchVacanciesInteractorImpl(get()) }
}

