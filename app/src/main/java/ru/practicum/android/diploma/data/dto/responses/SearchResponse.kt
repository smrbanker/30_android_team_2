package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.VacancyDto

data class SearchResponse(
    val items: List<VacancyDto>,
    val found: Int,
    val pages: Int,
    val page: Int,
) : Response()
