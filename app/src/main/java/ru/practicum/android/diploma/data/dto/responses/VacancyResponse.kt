package ru.practicum.android.diploma.data.dto.responses

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.VacancyDto

data class VacancyResponse(
    val found: Int, // Эти поля по сути не нужны - они нигде не испольлзуются, убираю?
    val pages: Int, // Эти поля по сути не нужны - они нигде не испольлзуются, убираю?
    val page: Int, // Эти поля по сути не нужны - они нигде не испольлзуются, убираю?
    @SerializedName("items")
    val vacancies: List<VacancyDto>
) : Response()
