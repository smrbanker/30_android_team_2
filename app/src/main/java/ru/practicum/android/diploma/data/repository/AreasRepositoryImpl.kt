package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.responses.FilterArea
import ru.practicum.android.diploma.domain.api.AreasRepository
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Resource

class AreasRepositoryImpl(private val networkClient: NetworkClient) : AreasRepository {
    private suspend fun getAreasResponse(): Response {
        return networkClient.doAreasRequest()
    }

    override suspend fun getCountries(): Resource<List<Country>> {
        val areasResponse = getAreasResponse()
        return when (areasResponse.resultCode) {
            RESULT_CODE_SUCCESS -> {
                val areaList = (areasResponse as FilterArea).areas
                Resource.Success(
                    areaList.map {
                        Country(
                            id = it.id,
                            name = it.name
                        )
                    }
                )
            }
            RESULT_CODE_NO_INTERNET -> Resource.Error(Resource.CONNECTION_PROBLEM)
            else -> Resource.Error(Resource.SERVER_ERROR)
        }
    }
}
