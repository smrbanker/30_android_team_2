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

    override fun clearAll() {
        filterSpRepository.clearAll()
    }

    override fun clearRegion() {
        filterSpRepository.clearRegion()
    }

    override fun clearIndustry() {
        filterSpRepository.clearIndustry()
    }

    override fun clearSalary() {
        filterSpRepository.clearSalary()
    }

    override fun onlyWithSalary() {
        filterSpRepository.onlyWithSalary()
    }

    override fun setSalary(value: String?) {
        filterSpRepository.setSalary(value)
    }

    override fun setStatus(status: Boolean) {
        filterSpRepository.setStatus(status)
    }
}
