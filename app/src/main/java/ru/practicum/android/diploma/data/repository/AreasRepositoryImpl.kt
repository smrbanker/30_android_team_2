package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.AreasDto
import ru.practicum.android.diploma.data.dto.responses.FilterArea
import ru.practicum.android.diploma.domain.api.AreasRepository
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
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

    override suspend fun getCountryRegions(parentId: Int): Resource<List<Region>> {
        val allAreasFromServer = getAreasResponse()
        return when (allAreasFromServer.resultCode) {
            RESULT_CODE_SUCCESS -> {
                val areaList = (allAreasFromServer as FilterArea).areas
                val regionList = mutableListOf<AreasDto>()
                areaList.forEach { country ->
                    if(country.id == parentId){
                        regionList.addAll(country.areas)
                    }
                }
                Resource.Success(
                    regionList.map {
                        Region(
                            id = it.id,
                            name = it.name,
                            parentId = it.parentId
                        )
                    }
                )
            }

            RESULT_CODE_NO_INTERNET -> Resource.Error(Resource.CONNECTION_PROBLEM)
            else -> Resource.Error(Resource.SERVER_ERROR)
        }
    }

    override suspend fun getAllRegions(): Resource<List<Region>> {
        val allAreasFromServer = getAreasResponse()
        return when (allAreasFromServer.resultCode) {
            RESULT_CODE_SUCCESS -> {
                val areaList = (allAreasFromServer as FilterArea).areas
                val regionList = mutableListOf<AreasDto>()
                areaList.forEach { country ->
                    regionList.addAll(country.areas)
                }
                Resource.Success(
                    regionList.map {
                        Region(
                            id = it.id,
                            name = it.name,
                            parentId = it.parentId
                        )
                    }
                )
            }

            RESULT_CODE_NO_INTERNET -> Resource.Error(Resource.CONNECTION_PROBLEM)
            else -> Resource.Error(Resource.SERVER_ERROR)
        }
    }
}
