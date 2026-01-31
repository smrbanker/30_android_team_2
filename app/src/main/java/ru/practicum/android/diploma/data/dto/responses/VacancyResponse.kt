package ru.practicum.android.diploma.data.dto.responses

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.VacancyDetail

data class VacancyResponse(
    val found: Int,
    val pages: Int,
    val page: Int,
    @SerializedName("items")
    val vacancies: List<VacancyDetail>
) : Response()
