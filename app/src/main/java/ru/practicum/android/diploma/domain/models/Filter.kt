package ru.practicum.android.diploma.domain.models

data class Filter(
    val location: Location = Location(),
    val sector: Sector? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false,
)

data class Location(
    val country: Country? = null,
    val region: Region? = null,
)

data class Sector(
    val id: Int,
    val name: String,
    val isChecked: Boolean
)

data class Country(
    val id: Int,
    val name: String
) : Area

data class Region(
    val id: Int,
    val name: String,
    val parentName: String,
    val parentId: Int
) : Area
