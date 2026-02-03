package ru.practicum.android.diploma.data.network

import android.content.Context
import retrofit2.HttpException
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.data.dto.RESULT_CODE_FORBIDDEN
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NOT_FOUND
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SERVER_ERROR
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.responses.SearchResponse
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitNetworkClient(
    private val jobApiService: JobApiService,
    private val context: Context,
) : NetworkClient {
    override suspend fun doCountryRequest(): Response {
        // if (!isConnected()) { // ПОКА ЗАКРЫЛ ИЗ-ЗА ANDROID_MANIFEST (СМ КОММЕНТАРИЙ НИЖЕ)
        //    return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        // }

        // TO DO

        // val response = // TO DO

        return Response().apply { resultCode }
    }

    override suspend fun doIndustryRequest(): Response {
        // if (!isConnected()) { // ПОКА ЗАКРЫЛ ИЗ-ЗА ANDROID_MANIFEST (СМ КОММЕНТАРИЙ НИЖЕ)
        //    return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        // }

        // TO DO

        // val response = // TO DO

        return Response().apply { resultCode }
    }

    override suspend fun doRegionRequest(id: String): Response {
        // if (!isConnected()) { // ПОКА ЗАКРЫЛ ИЗ-ЗА ANDROID_MANIFEST (СМ КОММЕНТАРИЙ НИЖЕ)
        //    return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        // }

        // TO DO

        // val response = // TO DO

        return Response().apply { resultCode }
    }

    override suspend fun doSearchRequest(options: Map<String, String>): Response {
        return try {
            val response = jobApiService.searchVacancies(options)
            SearchResponse(response).apply {
                resultCode = RESULT_CODE_SUCCESS
            }
        } catch (e: HttpException) {
            val errorCode = when (e.code()) {
                400 -> RESULT_CODE_BAD_REQUEST
                403 -> RESULT_CODE_FORBIDDEN
                404 -> RESULT_CODE_NOT_FOUND
                500 -> RESULT_CODE_SERVER_ERROR
                else -> RESULT_CODE_SERVER_ERROR
            }
            Response().apply { resultCode = errorCode }
        } catch (e: UnknownHostException) {
            Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        } catch (e: SocketTimeoutException) {
            Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        } catch (e: IOException) {
            Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        } catch (e: Exception) {
            Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
        }
    }

    override suspend fun doVacancyRequest(id: String): Response {
        // if (!isConnected()) { // ПОКА ЗАКРЫЛ ИЗ-ЗА ANDROID_MANIFEST (СМ КОММЕНТАРИЙ НИЖЕ)
        //    return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        // }

        // TO DO

        // val response = // TO DO

        return Response().apply { resultCode }
    }

    // ЭТА ФУНКЦИЯ БЕЗ НАСТРОЙКИ ANDROID_MANIFEST НЕ РАБОТАЕТ. ПРОВЕРИЛ, ВСЕ ОК, НО ПОКА ЗАКОММЕНТИРОВАЛ
    /*private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
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
