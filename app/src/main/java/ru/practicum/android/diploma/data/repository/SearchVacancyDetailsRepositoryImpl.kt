package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.models.vacancyToFullFromDetailResponse
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailResponse
import ru.practicum.android.diploma.domain.api.SearchVacancyDetailsRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchVacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
) : SearchVacancyDetailsRepository {

    override suspend fun searchVacancyDetails(id: String): Resource<Vacancy> {
        val vacancyResponse = networkClient.doVacancyRequest(id)
        return when (vacancyResponse.resultCode) {
            RESULT_CODE_SUCCESS -> {
                val vacancy = vacancyToFullFromDetailResponse(vacancyResponse as VacancyDetailResponse)
                val requestResult = Resource.Success(vacancy)
                requestResult
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

