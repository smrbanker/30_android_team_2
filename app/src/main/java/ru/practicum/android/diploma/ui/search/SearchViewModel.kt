package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchViewModel(
    private val interactor: SearchInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SearchState>(SearchState.Default)
    val state: LiveData<SearchState> = _state

    private var currentPage = 0
    private var maxPages = 0
    private var currentQuery: String = ""
    private var currentOptions: Map<String, String> = emptyMap()
    private val vacanciesList = mutableListOf<Vacancy>()
    private var isNextPageLoading = false
    private var isSearchInProgress = false

    fun search(query: String, additionalOptions: Map<String, String> = emptyMap()) {
        if (query.isEmpty()) {
            resetSearch()
            return
        }

        if (isSearchInProgress) return

        currentQuery = query
        currentPage = 0
        maxPages = 0
        vacanciesList.clear()
        isNextPageLoading = false

        val options = buildSearchOptions(query, additionalOptions, 0)
        currentOptions = additionalOptions

        performSearch(options, isFirstPage = true)
    }

    fun loadNextPage() {
        if (isNextPageLoading || currentPage >= maxPages || currentQuery.isEmpty()) {
            return
        }

        val nextPage = currentPage + 1
        val options = buildSearchOptions(currentQuery, currentOptions, nextPage)

        isNextPageLoading = true
        if (_state.value is SearchState.Content) {
            _state.value = (_state.value as SearchState.Content).copy(isLoadingMore = true)
        }

        performSearch(options, isFirstPage = false)
    }

    private fun performSearch(options: Map<String, String>, isFirstPage: Boolean) {
        if (isFirstPage) {
            isSearchInProgress = true
            _state.value = SearchState.Loading
        }

        viewModelScope.launch {
            val result = interactor.searchVacancies(options)

            when (result) {
                is ru.practicum.android.diploma.domain.models.Resource.Success -> {
                    val searchResult = result.data
                    maxPages = searchResult.pages
                    currentPage = searchResult.page

                    if (isFirstPage) {
                        vacanciesList.clear()
                        vacanciesList.addAll(searchResult.vacancies)
                    } else {
                        // Добавляем только новые вакансии, избегая дубликатов
                        val newVacancies = searchResult.vacancies.filter { newVacancy ->
                            vacanciesList.none { it.id == newVacancy.id }
                        }
                        vacanciesList.addAll(newVacancies)
                    }

                    isNextPageLoading = false
                    isSearchInProgress = false

                    if (vacanciesList.isEmpty()) {
                        _state.value = SearchState.Empty
                    } else {
                        _state.value = SearchState.Content(
                            vacancies = vacanciesList.toList(),
                            found = searchResult.found,
                            isLoadingMore = false
                        )
                    }
                }

                is ru.practicum.android.diploma.domain.models.Resource.Error -> {
                    isNextPageLoading = false
                    isSearchInProgress = false

                    if (isFirstPage) {
                        _state.value = SearchState.Error(result.message ?: "Произошла ошибка")
                    } else {
                        // Для ошибок при загрузке следующей страницы показываем Toast через событие
                        _state.value = SearchState.Error(result.message ?: "Произошла ошибка")
                        // Возвращаем предыдущее состояние
                        if (vacanciesList.isNotEmpty()) {
                            _state.value = SearchState.Content(
                                vacancies = vacanciesList.toList(),
                                found = (result.data?.found ?: 0).takeIf { it > 0 } ?: vacanciesList.size,
                                isLoadingMore = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun buildSearchOptions(
        query: String,
        additionalOptions: Map<String, String>,
        page: Int
    ): Map<String, String> {
        val options = mutableMapOf<String, String>()
        options["text"] = query
        options["per_page"] = "20"
        options["page"] = page.toString()

        additionalOptions.forEach { (key, value) ->
            if (value.isNotEmpty()) {
                options[key] = value
            }
        }

        return options
    }

    private fun resetSearch() {
        currentQuery = ""
        currentPage = 0
        maxPages = 0
        vacanciesList.clear()
        isNextPageLoading = false
        isSearchInProgress = false
        _state.value = SearchState.Default
    }

    fun onLastItemReached() {
        loadNextPage()
    }
}

