package ru.practicum.android.diploma.data.network

import android.content.Context
// import android.net.ConnectivityManager
// import android.net.NetworkCapabilities
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.requests.CountryRequest
import ru.practicum.android.diploma.data.dto.requests.IndustryRequest
import ru.practicum.android.diploma.data.dto.requests.RegionRequest
import ru.practicum.android.diploma.data.dto.requests.SearchRequest
import ru.practicum.android.diploma.data.dto.requests.VacancyRequest

class RetrofitNetworkClient(
    private val jobApiService: JobApiService,
    private val context: Context,
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }

        if ((dto !is CountryRequest)
            && (dto !is IndustryRequest)
            && (dto !is RegionRequest)
            && (dto !is SearchRequest)
            && (dto !is VacancyRequest)
        ) {
            return Response().apply { resultCode = RESULT_CODE_BAD_REQUEST }
        }

        if (dto is CountryRequest) { // TODO
        }
        if (dto is IndustryRequest) { // TODO
        }
        if (dto is RegionRequest) { // TODO
        }
        if (dto is SearchRequest) { // TODO
        }
        if (dto is VacancyRequest) { // TODO
        }

        val response = when (dto) {
            is CountryRequest -> {} // TODO
            is IndustryRequest -> {} // TODO
            is RegionRequest -> {} // TODO
            is SearchRequest -> {} // TODO
            else -> {} // VacancyRequest TODO
        }

        return Response().apply { resultCode }
    }

    // ЭТА ФУНКЦИЯ БЕЗ НАСТРОЙКИ ANDROID_MANIFEST НЕ РАБОТАЕТ. ПРОВЕРИЛ, ВСЕ ОК, НО ПОКА ЗАКОММЕНТИРОВАЛ
    private fun isConnected(): Boolean { return true }
    /*    val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }*/
}
