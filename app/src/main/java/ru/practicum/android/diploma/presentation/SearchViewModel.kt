package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.practicum.android.diploma.domain.api.FilterSpInteractor
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.VacancyState
import ru.practicum.android.diploma.util.debounce
import java.io.IOException
import java.net.SocketTimeoutException

class SearchViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val filterInteractor: FilterSpInteractor
) : ViewModel() {
    private val vacancyLiveData = MutableLiveData<VacancyState>()
    fun observeVacancy(): LiveData<VacancyState> = vacancyLiveData
    private var latestSearchText = ""
    private var currentPage = 1
    private var searchJob: Job? = null
    private val vacancySearchDebounce = debounce<String>(DEBOUNCE_DELAY, viewModelScope, true) { text ->
        search(text)
    }

    fun searchDebounce(text: String) {
        if (latestSearchText == text) return
        latestSearchText = text
        vacancySearchDebounce(text)
    }

    fun searchAnyway(text: String) {
        vacancySearchDebounce(text)
    }

    private fun search(text: String) {
        if (text.isNotEmpty()) {
            var state: VacancyState = VacancyState.Loading(false)
            vacancyLiveData.postValue(state)

            val filteredQuery = createFilteredQuery(text)
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                try {
                    state = vacancyInteractor.getVacancies(filteredQuery)
                    vacancyLiveData.postValue(state)
                } catch (_: IOException) {
                    vacancyLiveData.postValue(state)
                } catch (_: SocketTimeoutException) {
                    vacancyLiveData.postValue(state)
                } catch (_: HttpException) {
                    vacancyLiveData.postValue(state)
                }
            }
        }
    }

    fun loadMoreVacancies(text: String) {
        var state: VacancyState = VacancyState.Loading(true)
        vacancyLiveData.postValue(state)

        val filteredQuery = createFilteredQuery(text)

        viewModelScope.launch {
            try {
                state = vacancyInteractor.getVacancies(filteredQuery)
                vacancyLiveData.postValue(state)

                if (state is VacancyState.Content) {
                    val vacancies = (state as VacancyState.Content).vacanciesList
                    if (vacancies.isNotEmpty()) {
                        currentPage++
                    }
                }
            } catch (_: IOException) {
                vacancyLiveData.postValue(state)
            } catch (_: SocketTimeoutException) {
                vacancyLiveData.postValue(state)
            } catch (_: HttpException) {
                vacancyLiveData.postValue(state)
            }
        }
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
    }
}
