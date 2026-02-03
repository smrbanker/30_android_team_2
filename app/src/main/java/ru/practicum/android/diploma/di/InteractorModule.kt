package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl

val interactorModule = module {
    single<SearchInteractor> {
        SearchInteractorImpl(
            repository = get()
        )
    }
}
