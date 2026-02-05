package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Filter

interface FilterSpInteractor {
    fun input(filter: Filter)
    fun output(): Filter
}
