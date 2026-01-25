package ru.practicum.android.diploma.data.dto.models

data class CountryDto(
    val id: Int,
    val name: String,
    val areas: List<RegionDto>
)
