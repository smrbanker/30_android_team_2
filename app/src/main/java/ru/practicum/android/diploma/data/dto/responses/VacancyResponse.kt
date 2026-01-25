package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.models.VacancyDto

data class VacancyResponse(
    val found: Int,
    val pages: Int,
    val page: Int,
    val vacancies: List<VacancyDto>
)
