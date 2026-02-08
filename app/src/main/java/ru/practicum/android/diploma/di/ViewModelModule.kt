package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.FiltrationCountryViewModel
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.ui.favorites.fragment.FavouritesViewModel
import ru.practicum.android.diploma.ui.filtration.FiltrationSettingsViewModel
import ru.practicum.android.diploma.ui.vacancy.fragment.VacancyViewModel

val viewModelModule = module {

    viewModel {
        FavouritesViewModel(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }
    viewModel {
        VacancyViewModel(
            get(),
            get(),
            androidContext()
        )
    }

    viewModel {
        FiltrationSettingsViewModel(get())
    }

    viewModel {
        FiltrationCountryViewModel(get(), get())
    }
}
