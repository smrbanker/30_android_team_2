package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Resource

interface AreasRepository {
    suspend fun getCountries(): Resource<List<Country>>
}
