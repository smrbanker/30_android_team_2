package ru.practicum.android.diploma.domain.models

sealed class FilterState {
    data class Area(
        val areaId: String,
        val areaName: String,
        val regionId: String,
        val regionName: String
    ) : FilterState()

    data class Industry(
        val industryId: String,
        val industryName: String
    ) : FilterState()

    data class Salary(val salary: Int) : FilterState()

    data class OnlyWithSalary(val onlyWithSalary: Boolean) : FilterState()
}
