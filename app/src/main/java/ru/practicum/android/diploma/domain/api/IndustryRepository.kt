package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.models.industryDtoToDomain
import ru.practicum.android.diploma.data.dto.responses.FilterIndustry
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Resource

interface IndustryRepository {
    suspend fun getIndustries(): Resource<List<Industry>>

}
