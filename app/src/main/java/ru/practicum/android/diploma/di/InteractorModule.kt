package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.db.FavouritesInteractor
import ru.practicum.android.diploma.domain.impl.FavouritesInteractorImpl

val interactorModule = module {

    factory<FavouritesInteractor> {
        FavouritesInteractorImpl(get())
    }
}
