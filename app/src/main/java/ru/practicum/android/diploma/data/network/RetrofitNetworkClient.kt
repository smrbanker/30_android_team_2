package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SERVER_ERROR
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(
    private val jobApiService: JobApiService,
    private val context: Context,
) : NetworkClient {

    override suspend fun doCountryRequest(): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = jobApiService.getCountries()
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

    override suspend fun doIndustryRequest(): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }

        return withContext(Dispatchers.IO) {
            try {
                val industryResponse = jobApiService.getIndustries()
                industryResponse.apply { resultCode = RESULT_CODE_SUCCESS }
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            } catch (e: HttpException) {
                e.printStackTrace()
                return@withContext Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }

    override suspend fun doRegionRequest(id: String): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = jobApiService.getRegions(id) // Предположим, у вас есть этот метод
                response.apply { resultCode = RESULT_CODE_SUCCESS }
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            } catch (e: HttpException) {
                e.printStackTrace()
                return@withContext Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
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
                return@withContext Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
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
                return@withContext Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false

        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return capabilities != null && (
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            )
    }
}
