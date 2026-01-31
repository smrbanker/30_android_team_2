package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.VacancyDto

//Файл к удалению
data class VacancyDetail(
    val vacancyDto: VacancyDto
) : Response()
