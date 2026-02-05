package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FilterSpInteractor
import ru.practicum.android.diploma.domain.api.FilterSpRepository
import ru.practicum.android.diploma.domain.models.Filter

class FilterSpInteractorImpl(
    private val filterSpRepository: FilterSpRepository,
) : FilterSpInteractor {

    override fun input(filter: Filter) =
        filterSpRepository.input(filter)

    override fun output(): Filter =
        filterSpRepository.output()
}
