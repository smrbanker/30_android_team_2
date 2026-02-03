package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.data.dto.Response

data class SearchResponse(
    val vacancyResponse: VacancyResponse
) : Response() {
    init {
        resultCode = RESULT_CODE_SUCCESS
    }
}

