package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SERVER_ERROR
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(
    private val jobApiService: JobApiService,
    private val context: Context,
) : NetworkClient {
    override suspend fun doAreasRequest(): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }
        return withContext(Dispatchers.IO) {
            try {
                jobApiService.getAreas().apply { resultCode = RESULT_CODE_SUCCESS }
            } catch (e: IOException) {
                e.printStackTrace()
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }

    override suspend fun doIndustryRequest(): Response {
        // if (!isConnected()) { // ПОКА ЗАКРЫЛ ИЗ-ЗА ANDROID_MANIFEST (СМ КОММЕНТАРИЙ НИЖЕ)
        //    return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        // }

        // TO DO

        // val response = // TO DO

        return Response().apply { resultCode }
    }

    override suspend fun doRegionRequest(): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }
        return withContext(Dispatchers.IO) {
            try {
                jobApiService.getAreas().apply { resultCode = RESULT_CODE_SUCCESS }
            } catch (e: IOException) {
                e.printStackTrace()
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }

    override suspend fun doSearchRequest(options: Map<String, String>): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }
        return withContext(Dispatchers.IO) {
            try {
                jobApiService.searchVacancies(options).apply { resultCode = RESULT_CODE_SUCCESS }
            } catch (e: IOException) {
                e.printStackTrace()
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }

    override suspend fun doVacancyRequest(id: String): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        } else {
            return withContext(Dispatchers.IO) {
                try {
                    val response = jobApiService.getVacancy(id)
                    response.apply { resultCode = RESULT_CODE_SUCCESS }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
                }
            }
        }

    }

    // ЭТА ФУНКЦИЯ БЕЗ НАСТРОЙКИ ANDROID_MANIFEST НЕ РАБОТАЕТ. ПРОВЕРИЛ, ВСЕ ОК, НО ПОКА ЗАКОММЕНТИРОВАЛ
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
