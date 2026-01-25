package ru.practicum.android.diploma.ui.main

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.practicum.android.diploma.di.dataModule
import ru.practicum.android.diploma.di.repositoryModule
import ru.practicum.android.diploma.di.interactorModule
import ru.practicum.android.diploma.di.viewModelModule
import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }
}
