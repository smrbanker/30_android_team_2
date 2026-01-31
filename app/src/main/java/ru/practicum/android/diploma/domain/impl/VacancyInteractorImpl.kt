package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyState

class VacancyInteractorImpl(private val repository: VacancyRepository) : VacancyInteractor {
    override suspend fun getVacancies(filteredQuery: Map<String, String>): VacancyState {
        return when (val resource = repository.getVacancies(filteredQuery)) {
            is Resource.Success -> {
                handleSuccess(requireNotNull(resource.data))
            }
            is Resource.Error -> handleError(requireNotNull(resource.message))
        }
    }
    private fun handleSuccess(vacanciesList: List<Vacancy>): VacancyState {
        return if (vacanciesList.isEmpty()) {
            VacancyState.Empty
        } else {
            VacancyState.Content(vacanciesList)
        }
    }

    private fun handleError(errorMessage: String): VacancyState {
        return VacancyState.Error(errorMessage)
    }
}
