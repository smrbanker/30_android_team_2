package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.api.SearchVacancyDetailsInteractor
import ru.practicum.android.diploma.domain.api.SearchVacancyDetailsRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchVacancyDetailsInteractorImpl(private val repository: SearchVacancyDetailsRepository) :
    SearchVacancyDetailsInteractor {
    override fun searchVacancyDetails(id: String): Flow<Pair<Vacancy?, String?>> {
        return repository.searchVacancyDetails(id).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}
