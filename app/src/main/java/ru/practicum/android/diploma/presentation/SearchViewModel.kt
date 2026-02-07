package ru.practicum.android.diploma.presentation

import android.util.Log
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
    private var isLoading = false
    private var searchJob: Job? = null
    private val vacancySearchDebounce = debounce<String>(DEBOUNCE_DELAY, viewModelScope, true) { text ->
        search(text)
    }

    fun searchDebounce(text: String) {
        if (latestSearchText == text) return
        latestSearchText = text
        vacancySearchDebounce(text)
    }

    private fun search(text: String) {
        if (text.isNotEmpty()) {
            setState(VacancyState.Loading)

            val filteredQuery = createFilteredQuery()
            filteredQuery["text"] = text
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                Log.d("ASD", "Inside launch")
                vacancyLiveData.postValue(vacancyInteractor.getVacancies(filteredQuery))
            }
        }
    }

    private fun setState(state: VacancyState) {
        vacancyLiveData.postValue(state)
    }

    companion object {
        private const val DEBOUNCE_DELAY = 2000L
    }

    fun loadMoreVacancies(): LiveData<VacancyState> {
        val resultLiveData = MutableLiveData<VacancyState>()

        if (isLoading) return resultLiveData
        isLoading = true

        viewModelScope.launch {
            val filteredQuery = createFilteredQuery()

            try {
                val state = vacancyInteractor.getVacancies(filteredQuery)
                handleVacancyState(state, resultLiveData)
            } catch (e: IOException) {
                handleError(resultLiveData, "Ошибка сети: ${e.message}")
            } catch (e: SocketTimeoutException) {
                handleError(resultLiveData, "Время ожидания соединения истекло: ${e.message}")
            } catch (e: HttpException) {
                handleError(resultLiveData, "Ошибка подключения: ${e.message}")
            } finally {
                isLoading = false
            }
        }

        return resultLiveData
    }

    private fun createFilteredQuery(): HashMap<String, String> {
        val filter: Filter = filterInteractor.output()
        val filteredQuery = HashMap<String, String>()
        filteredQuery["page"] = currentPage.toString()

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

    private fun handleVacancyState(state: VacancyState, resultLiveData: MutableLiveData<VacancyState>) {
        when (state) {
            is VacancyState.Content -> {
                val vacancies = state.vacanciesList
                if (vacancies.isNotEmpty()) {
                    currentPage++
                    resultLiveData.postValue(VacancyState.Content(vacancies, vacancies.size))
                } else {
                    resultLiveData.postValue(VacancyState.Empty)
                }
            }
            is VacancyState.Error -> {
                resultLiveData.postValue(VacancyState.Error(state.errorMessage))
            }
            is VacancyState.Empty -> {
                resultLiveData.postValue(VacancyState.Empty)
            }
            is VacancyState.Loading -> {
                resultLiveData.postValue(VacancyState.Loading)
            }
        }
    }

    private fun handleError(resultLiveData: MutableLiveData<VacancyState>, message: String) {
        resultLiveData.postValue(VacancyState.Error(message))
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
}
