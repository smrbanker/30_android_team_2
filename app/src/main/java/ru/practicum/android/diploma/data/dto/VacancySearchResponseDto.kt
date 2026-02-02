package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancySearchResponseDto(
    @SerializedName("items")
    val items: List<VacancyDto>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("found")
    val found: Int,
    @SerializedName("per_page")
    val perPage: Int
)

