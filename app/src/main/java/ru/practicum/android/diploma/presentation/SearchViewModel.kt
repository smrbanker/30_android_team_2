package ru.practicum.android.diploma.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.VacancyState
import ru.practicum.android.diploma.util.debounce
import java.io.IOException
import java.net.SocketTimeoutException

class SearchViewModel(private val vacancyInteractor: VacancyInteractor) : ViewModel() {
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

            val filteredQuery = HashMap<String, String>()
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
        val filteredQuery = HashMap<String, String>()
        filteredQuery["page"] = currentPage.toString()
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
}
