package ru.practicum.android.diploma.data.dto.models

data class AreasDto(
    val id: Int,
    val name: String,
    val parentId: Int,
    val areas: List<AreasDto>
)
