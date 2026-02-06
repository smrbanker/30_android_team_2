package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterState

interface FilterRepository {
    val filterAreaState: Flow<FilterState.Area?>
    val filterIndustryState: Flow<FilterState.Industry?>
    val filterSalaryState: Flow<FilterState.Salary?>
    val filterOnlyWithSalaryState: Flow<FilterState.OnlyWithSalary?>

    fun updateArea(
        areaId: String,
        areaName: String,
        regionId: String,
        regionName: String
    )

    fun updateIndustry(
        industryId: String,
        industryName: String
    )

    fun updateSalary(salary: Int)

    fun updateOnlyWithSalary(onlyWithSalary: Boolean)

    fun clearFilters()
}
