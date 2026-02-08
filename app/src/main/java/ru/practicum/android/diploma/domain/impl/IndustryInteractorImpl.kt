package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.IndustryInteractor
import ru.practicum.android.diploma.domain.api.IndustryRepository
import ru.practicum.android.diploma.domain.models.IndustryState
import ru.practicum.android.diploma.domain.models.Resource

class IndustryInteractorImpl(private val repository: IndustryRepository) : IndustryInteractor {
    override suspend fun getIndustries(): IndustryState {
        return when (val resource = repository.getIndustries()) {
            is Resource.Success -> {
                handleSuccess(requireNotNull(resource.data))
            }
            is Resource.Error -> handleError(requireNotNull(resource.message))
        }
    }

    private fun handleSuccess(
        industriesList: List<ru.practicum.android.diploma.domain.models.Industry>
    ): IndustryState {
        return if (industriesList.isEmpty()) {
            IndustryState.Empty
        } else {
            IndustryState.Content(industriesList)
        }
    }

    private fun handleError(errorMessage: String): IndustryState {
        return IndustryState.Error(errorMessage)
    }
}
