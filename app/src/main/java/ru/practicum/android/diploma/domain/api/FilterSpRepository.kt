package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Filter

interface FilterSpRepository {
    fun input(filter: Filter)
    fun output(): Filter
    fun clear()
}
