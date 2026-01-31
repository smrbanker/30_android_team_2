package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.models.vacancyToFull
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailResponse
import ru.practicum.android.diploma.domain.api.SearchVacancyDetailsRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchVacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
) : SearchVacancyDetailsRepository {

    override fun searchVacancyDetails(id: String): Flow<Resource<Vacancy>> = flow {

        val vacancyResponse = networkClient.doVacancyRequest(id)

        when (vacancyResponse.resultCode) {
            RESULT_CODE_SUCCESS -> {
                val vacancy = vacancyToFull((vacancyResponse as VacancyDetailResponse).vacancyDetail)
                val requestResult =  Resource.Success( vacancy )
                emit(requestResult)
            }
            RESULT_CODE_NO_INTERNET -> {
                emit(Resource.Error(Resource.CONNECTION_PROBLEM))
            }
            else -> {
                emit (Resource.Error( Resource.SERVER_ERROR))
            }
        }
    }

}

