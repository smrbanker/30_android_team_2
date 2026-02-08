package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.models.industryDtoToDomain
import ru.practicum.android.diploma.data.dto.responses.FilterIndustry
import ru.practicum.android.diploma.domain.api.IndustryRepository
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Resource

class IndustryRepositoryImpl(private val networkClient: NetworkClient) : IndustryRepository {

    override suspend fun getIndustries(): Resource<List<Industry>> {
        val response = networkClient.doIndustryRequest()
        return when (response.resultCode) {
            RESULT_CODE_SUCCESS -> {
                if (response is FilterIndustry) {
                    Resource.Success(
                        response.industries.map {
                            industryDtoToDomain(it)
                        }
                    )
                } else {
                    Resource.Error(Resource.SERVER_ERROR)
                }
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
