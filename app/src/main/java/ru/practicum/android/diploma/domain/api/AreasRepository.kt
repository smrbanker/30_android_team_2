package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.Resource

interface AreasRepository {
    suspend fun getCountries(): Resource<List<Country>>
    suspend fun getCountryRegions(parentName: String): Resource<List<Region>>
    suspend fun getAllRegions(): Resource<List<Region>>
}
