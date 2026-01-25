package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.CountryDto

data class CountryResponse(
    val countries: List<CountryDto>
) : Response()
