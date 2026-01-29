package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.AreasDto

data class FilterArea(
    val areas: List<AreasDto>
) : Response()
