package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Resource

interface IndustryRepository {
    suspend fun getIndustries(): Resource<List<Industry>>

}
