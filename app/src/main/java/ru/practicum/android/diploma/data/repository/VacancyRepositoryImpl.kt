package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.models.vacancyToFull
import ru.practicum.android.diploma.data.dto.responses.VacancyResponse
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyRepositoryImpl(private val networkClient: NetworkClient) : VacancyRepository {
    override suspend fun getVacancies(filteredQuery: Map<String, String>): Resource<List<Vacancy>> {
        val response = networkClient.doSearchRequest(filteredQuery)
        return when (response.resultCode) {
            RESULT_CODE_SUCCESS -> {
                Resource.Success((response as VacancyResponse).vacancies.map {
                    vacancyToFull(it)
                })
            }
            RESULT_CODE_NO_INTERNET -> {
                Resource.Error(Resource.CONNECTION_PROBLEM)
            }
            else -> {
                Resource.Error(Resource.SERVER_ERROR)
            }
        }
    }
}
