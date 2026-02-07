package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Country

interface AreasInteractor {
    suspend fun getCountries(): Pair<List<Country>?, String?>
}
