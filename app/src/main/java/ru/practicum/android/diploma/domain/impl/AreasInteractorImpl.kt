package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.AreasInteractor
import ru.practicum.android.diploma.domain.api.AreasRepository
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Resource

class AreasInteractorImpl(private val areasRepository: AreasRepository) : AreasInteractor {
    override suspend fun getCountries(): Pair<List<Country>?, String?> {
        return when (val resource = areasRepository.getCountries()) {
            is Resource.Success -> Pair(resource.data, null)
            is Resource.Error -> Pair(null, resource.message)
        }
    }
}
