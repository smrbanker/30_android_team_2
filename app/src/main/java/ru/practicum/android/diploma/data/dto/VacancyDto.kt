package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("salary")
    val salary: SalaryDto?,
    @SerializedName("employer")
    val employer: EmployerDto?,
    @SerializedName("area")
    val area: AreaDto?,
    @SerializedName("snippet")
    val snippet: SnippetDto?
)

data class SalaryDto(
    @SerializedName("from")
    val from: Int?,
    @SerializedName("to")
    val to: Int?,
    @SerializedName("currency")
    val currency: String?
)

data class EmployerDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("logo_urls")
    val logoUrls: LogoUrlsDto?
)

data class LogoUrlsDto(
    @SerializedName("original")
    val original: String?
)

data class AreaDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)

data class SnippetDto(
    @SerializedName("requirement")
    val requirement: String?,
    @SerializedName("responsibility")
    val responsibility: String?
)

