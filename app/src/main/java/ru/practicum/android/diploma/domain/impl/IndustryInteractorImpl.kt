package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Resource

class IndustryInteractorImpl (
    private val industryRepository: IndustryRepository
) : IndustryInteractor {

    override suspend fun getIndustries(): Pair<List<Industry>?, String?> {
        return when (val resource = industryRepository.getIndustries()) {
            is Resource.Success -> Pair (
                first = resource.data

                second = null
            )
            is Resource.Error -> Pair (first = null, second = resource.message)
        }
    }
}
