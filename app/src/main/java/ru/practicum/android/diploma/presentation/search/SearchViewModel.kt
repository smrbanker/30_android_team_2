package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.impl.NoInternetException
import ru.practicum.android.diploma.domain.impl.NetworkException
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchViewModel(
    private val interactor: SearchVacanciesInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SearchState>(SearchState.Initial)
    val state: LiveData<SearchState> = _state

    private val _isLoadingNextPage = MutableLiveData<Boolean>(false)
    val isLoadingNextPage: LiveData<Boolean> = _isLoadingNextPage

    private var currentQuery: String = ""
    private var currentPage: Int = 0
    private var maxPages: Int = 0
    private var vacanciesList: MutableList<Vacancy> = mutableListOf()
    private var isNextPageLoading: Boolean = false
    private var searchJob: Job? = null

    fun searchVacancies(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isBlank()) {
                _state.value = SearchState.Initial
                vacanciesList.clear()
                currentPage = 0
                maxPages = 0
                return@launch
            }

            currentQuery = query
            currentPage = 0
            maxPages = 0
            vacanciesList.clear()
            _state.value = SearchState.Loading

            delay(300) // debounce

            loadPage(query, 0, isFirstPage = true)
        }
    }

    fun loadNextPage() {
        if (isNextPageLoading || currentPage >= maxPages || currentQuery.isBlank()) {
            return
        }

        viewModelScope.launch {
            isNextPageLoading = true
            _isLoadingNextPage.value = true
            loadPage(currentQuery, currentPage + 1, isFirstPage = false)
        }
    }

    private suspend fun loadPage(query: String, page: Int, isFirstPage: Boolean) {
        try {
            val result = interactor.searchVacancies(query, page, PER_PAGE)

            result.onSuccess { searchResult ->
                if (isFirstPage) {
                    vacanciesList.clear()
                }

                val newVacancies = searchResult.vacancies.filter { vacancy ->
                    !vacanciesList.any { it.id == vacancy.id }
                }
                vacanciesList.addAll(newVacancies)

                currentPage = searchResult.page
                maxPages = searchResult.pages

                if (vacanciesList.isEmpty()) {
                    _state.value = SearchState.Empty
                } else {
                    _state.value = SearchState.Content(
                        vacancies = vacanciesList.toList(),
                        found = searchResult.found
                    )
                }
            }.onFailure { exception ->
                val errorMessage = when (exception) {
                    is NoInternetException -> "Проверьте подключение к интернету"
                    is NetworkException -> "Произошла ошибка"
                    else -> "Произошла ошибка: ${exception.message}"
                }

                if (isFirstPage) {
                    _state.value = SearchState.Error(errorMessage)
                } else {
                    // Для последующих страниц показываем Toast через отдельный LiveData
                    _state.value = SearchState.Error(errorMessage)
                }
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is NoInternetException -> "Проверьте подключение к интернету"
                is NetworkException -> "Произошла ошибка"
                else -> "Произошла ошибка: ${e.message}"
            }

            if (isFirstPage) {
                _state.value = SearchState.Error(errorMessage)
            } else {
                _state.value = SearchState.Error(errorMessage)
            }
        } finally {
            isNextPageLoading = false
            _isLoadingNextPage.value = false
        }
    }

    fun onLastItemReached() {
        if (!isNextPageLoading && currentPage < maxPages) {
            loadNextPage()
        }
    }

    companion object {
        private const val PER_PAGE = 20
    }
}

