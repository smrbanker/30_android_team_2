package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.RegionDto

data class RegionResponse(
    val regions: List<RegionDto>
) : Response()
