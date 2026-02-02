package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.mapper.VacancyMapper
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient
) : VacancyRepository {

    override suspend fun searchVacancies(
        query: String,
        page: Int,
        perPage: Int
    ): Result<VacancySearchResult> {
        return try {
            if (!networkClient.isInternetAvailable()) {
                return Result.failure(NoInternetException("Нет подключения к интернету"))
            }

            val headers = mapOf(
                "Authorization" to "Bearer ${ru.practicum.android.diploma.BuildConfig.API_ACCESS_TOKEN}",
                "HH-User-Agent" to "DiplomaApp (diploma@example.com)"
            )

            val params = mutableMapOf<String, String>().apply {
                put("text", query)
                put("page", page.toString())
                put("per_page", perPage.toString())
            }

            val response = networkClient.apiService.searchVacancies(headers, params)
            
            val vacancies = response.items.map { it.toDomain() }
            val result = VacancySearchResult(
                vacancies = vacancies,
                page = response.page,
                pages = response.pages,
                found = response.found,
                perPage = response.perPage
            )

            Result.success(result)
        } catch (e: UnknownHostException) {
            Result.failure(NoInternetException("Проверьте подключение к интернету"))
        } catch (e: SocketTimeoutException) {
            Result.failure(NetworkException("Превышено время ожидания ответа"))
        } catch (e: IOException) {
            Result.failure(NetworkException("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(NetworkException("Произошла ошибка: ${e.message}"))
        }
    }
}

class NoInternetException(message: String) : Exception(message)
class NetworkException(message: String) : Exception(message)

