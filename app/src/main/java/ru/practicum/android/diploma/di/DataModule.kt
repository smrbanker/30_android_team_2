package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.network.NetworkClient

val dataModule = module {
    single { NetworkClient(androidContext()) }
}

