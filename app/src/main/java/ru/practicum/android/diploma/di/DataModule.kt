package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.network.JobApiService
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.db.Database

val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), Database::class.java, "database.db")
            .build()
    }

    single { get<Database>().FavoritesDao() }

    factory { Gson() }
    single<JobApiService> {
        Retrofit.Builder()
            .baseUrl("https://practicum-diploma-8bc38133faba.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(JobApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidApplication())
    }

    single {
        androidContext()
            .getSharedPreferences("settings_list", Context.MODE_PRIVATE)
    }


}
