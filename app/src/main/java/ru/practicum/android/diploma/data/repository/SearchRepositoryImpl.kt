package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.data.dto.RESULT_CODE_FORBIDDEN
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NOT_FOUND
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SERVER_ERROR
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.responses.SearchResponse
import ru.practicum.android.diploma.data.dto.responses.VacancyResponse
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.SearchResult
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.data.dto.models.vacancyToFull

class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {

    override suspend fun searchVacancies(options: Map<String, String>): Resource<SearchResult> {
        val response = networkClient.doSearchRequest(options)

        return when (response.resultCode) {
            RESULT_CODE_SUCCESS -> {
                val searchResponse = response as SearchResponse
                val vacancyResponse = searchResponse.vacancyResponse
                val vacancies = vacancyResponse.vacancies.map { vacancyToFull(it) }
                Resource.Success(
                    SearchResult(
                        found = vacancyResponse.found,
                        pages = vacancyResponse.pages,
                        page = vacancyResponse.page,
                        vacancies = vacancies
                    )
                )
            }
            RESULT_CODE_NO_INTERNET -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            RESULT_CODE_BAD_REQUEST -> {
                Resource.Error("Некорректный запрос")
            }
            RESULT_CODE_FORBIDDEN -> {
                Resource.Error("Доступ запрещен")
            }
            RESULT_CODE_NOT_FOUND -> {
                Resource.Error("Ресурс не найден")
            }
            RESULT_CODE_SERVER_ERROR -> {
                Resource.Error("Произошла ошибка")
            }
            else -> {
                Resource.Error("Произошла ошибка")
            }
        }
    }
}

