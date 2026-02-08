package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Filter

interface FilterSpRepository {
    fun input(filter: Filter)
    fun output(): Filter
    fun clearAll()
    fun clearRegion()
    fun clearIndustry()
    fun clearSalary()
    fun onlyWithSalary()
    fun setSalary(value: String?)
    fun setStatus(status: Boolean)
}
