package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SERVER_ERROR
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.responses.FilterIndustry

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


    object NetworkConstants {
        const val TAG_NETWORK_REQUEST = "NetworkRequest"
        const val MESSAGE_SUCCESS = "Успешно получен ответ по индустриям: "
        const val MESSAGE_ERROR_NETWORK = "Ошибка сети: "
        const val MESSAGE_ERROR_HTTP = "Ошибка HTTP: "
    }

    override suspend fun doIndustryRequest(): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }

        return withContext(Dispatchers.IO) {
            try {
                jobApiService.getIndustries().apply { RESULT_CODE_SUCCESS }
                FilterIndustry(jobApiService.getIndustries()).apply { resultCode = RESULT_CODE_SUCCESS }
            } catch (e: IOException) {
                e.printStackTrace()
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
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
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }

        return withContext(Dispatchers.IO) {
            try {
                val vacancyResponse = jobApiService.searchVacancies(options)
                vacancyResponse.apply { resultCode = RESULT_CODE_SUCCESS }
            } catch (e: IOException) {
                e.printStackTrace()
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            } catch (e: HttpException) {
                e.printStackTrace()
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }

    override suspend fun doVacancyRequest(id: String): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = jobApiService.getVacancy(id)
                response.apply { resultCode = RESULT_CODE_SUCCESS }
            } catch (e: IOException) {
                e.printStackTrace()
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            } catch (e: HttpException) {
                e.printStackTrace()
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: run {
            Log.e("NetworkCheck", "Нет активной сети")
            return false
        }

        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isConnected = capabilities != null && (
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            )

        Log.d("NetworkCheck", "Состояние подключения: $isConnected")
        return isConnected
    }
}
