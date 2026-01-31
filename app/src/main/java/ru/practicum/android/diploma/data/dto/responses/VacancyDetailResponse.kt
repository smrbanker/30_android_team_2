package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.VacancyDetail

data class VacancyDetailResponse(
    val vacancyDetail: VacancyDetail
) : Response()
