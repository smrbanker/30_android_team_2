package ru.practicum.android.diploma.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.practicum.android.diploma.domain.api.FilterSpInteractor
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyState
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val filterInteractor: FilterSpInteractor
) : ViewModel() {
    // Это "кусочек" поискового запроса, который отдает нам АПИ
    private val vacancyLiveData = MutableLiveData<VacancyState>()
    fun observeVacancy(): LiveData<VacancyState> = vacancyLiveData

    private val inputLiveData = MutableLiveData<String>()
    fun observeInput(): LiveData<String> = inputLiveData

    // Эти три строки отвечают за состояние экрана поиска по возврату на него
    // vacancyState - это "склад", где мы храним все вакансии по запросу
    private val vacancyState = mutableListOf<Vacancy>()
    private val vacancyStateLiveData = MutableLiveData<Pair<List<Vacancy>, Int>>()
    fun observeState(): LiveData<Pair<List<Vacancy>, Int>> = vacancyStateLiveData

    private var latestSearchText = ""
    private var currentPage = 1
    private var searchJob: Job? = null
    private val vacancySearchDebounce = debounce<String>(DEBOUNCE_DELAY, viewModelScope, true) { text ->
        search(text)
    }

    fun searchDebounce(text: String) {
        if (latestSearchText == text) return
        latestSearchText = text
        inputLiveData.postValue(text)
        vacancySearchDebounce(text)
    }

    fun searchAnyway(text: String) {
        vacancySearchDebounce(text)
    }

    fun delayToast() {
        runBlocking {
            delay(TOAST_DELAY)
        }
    }

    private fun search(text: String) {
        Log.d("RENDER", "search")
        if (text.isNotEmpty()) {
            var state: VacancyState = VacancyState.Loading(false)
            vacancyLiveData.postValue(state)

            vacancyState.clear()
            currentPage = 1
            val filteredQuery = createFilteredQuery(text)
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                state = vacancyInteractor.getVacancies(filteredQuery)
                vacancyLiveData.postValue(state)
                if (state is VacancyState.Content) {
                    val stateContent = state as VacancyState.Content
                    updateState(stateContent.vacanciesList, stateContent.itemsFound)
                }
            }
        }
    }

    fun loadMoreVacancies(text: String) {
        Log.d("RENDER", "loadMoreVacancies")
        var state: VacancyState = VacancyState.Loading(true)
        vacancyLiveData.postValue(state)

        currentPage++
        val filteredQuery = createFilteredQuery(text)

        viewModelScope.launch {
            state = vacancyInteractor.getVacancies(filteredQuery)
            if (state is VacancyState.Content) {
                val stateContent = state as VacancyState.Content
                updateState(stateContent.vacanciesList, stateContent.itemsFound)
            }
            vacancyLiveData.postValue(state)
        }
    }


    private fun updateState(vacancyList: List<Vacancy>, itemsFound: Int) {
        vacancyState.addAll(vacancyList)
        vacancyStateLiveData.postValue(vacancyState to itemsFound)
    }

    private fun createFilteredQuery(text: String): HashMap<String, String> {
        val filter: Filter = filterInteractor.output()
        val filteredQuery = HashMap<String, String>()
        filteredQuery["page"] = currentPage.toString()
        filteredQuery["text"] = text

        if (filter.location.country != null) {
            if (filter.location.region != null) {
                filteredQuery["area"] = filter.location.region.id.toString()
            } else {
                filteredQuery["area"] = filter.location.country.id.toString()
            }
        }

        if (filter.sector != null) {
            filteredQuery["industry"] = filter.sector.id.toString()
        }

        if (filter.salary != null) {
            filteredQuery["salary"] = filter.salary.toString()
        }

        filteredQuery["only_with_salary"] = filter.onlyWithSalary.toString()

        return filteredQuery
    }

    fun checkFilterButton(): Boolean {
        var flag = false
        runBlocking {
            val filter = filterInteractor.output()
            if (filter.location.country != null || filter.location.region != null) {
                flag = true
            }
            if (filter.sector != null || filter.salary != null || filter.onlyWithSalary) {
                flag = true
            }
        }
        return flag
    }

    companion object {
        private const val DEBOUNCE_DELAY = 2000L
        private const val TOAST_DELAY = 1000L
    }
}
