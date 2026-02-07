package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Resource

interface AreasInteractor {
    suspend fun getCountries(): Pair<List<Country>?, String?>
}
