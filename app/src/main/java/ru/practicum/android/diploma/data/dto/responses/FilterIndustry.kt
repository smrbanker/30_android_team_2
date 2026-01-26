package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.IndustryDto

data class FilterIndustry(
    val industries: List<IndustryDto>
) : Response()
