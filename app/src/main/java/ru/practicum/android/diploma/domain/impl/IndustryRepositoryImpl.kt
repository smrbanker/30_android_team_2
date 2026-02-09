package ru.practicum.android.diploma.domain.impl

import okhttp3.Response
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.responses.FilterIndustry
import ru.practicum.android.diploma.domain.api.IndustryRepository
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Resource

class IndustryRepositoryImpl(
    private val networkClient: NetworkClient
) : IndustryRepository {

    private suspend fun getIndustryResponse(): Response {
        return networkClient.doIndustryRequest()
    }

    override suspend fun getIndustries(): Resource<List<Industry>> {
        val industriesResponse = getIndustryResponse()
        return when (industriesResponse.resultCode) {
            RESULT_CODE_SUCCESS -> {
                val industryList = (industriesResponse as FilterIndustry).industries
                Resource.Success(
                    data = industryList.map {
                        Industry(
                            id = it.id,
                            name = it.name
                        )
                    }
                )
            }
            RESULT_CODE_NO_INTERNET -> Resource.Error(message = Resource.CONNECTION_PROBLEM)
            else -> Resource.Error(message = Resource.SERVER_ERROR)
        }
    }
}
