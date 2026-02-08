package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region

interface AreasInteractor {
    suspend fun getCountries(): Pair<List<Country>?, String?>
    suspend fun getCountryRegions(parentId: Int): Pair<List<Region>?, String?>
    suspend fun getAllRegions(): Pair<List<Region>?, String?>
}
