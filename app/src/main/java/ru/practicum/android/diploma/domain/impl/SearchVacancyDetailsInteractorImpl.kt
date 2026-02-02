package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SearchVacancyDetailsInteractor
import ru.practicum.android.diploma.domain.api.SearchVacancyDetailsRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchVacancyDetailsInteractorImpl(private val repository: SearchVacancyDetailsRepository) :
    SearchVacancyDetailsInteractor {

    override suspend fun searchVacancyDetails(id: String): Resource<Vacancy> {
        return repository.searchVacancyDetails(id)
    }
}
