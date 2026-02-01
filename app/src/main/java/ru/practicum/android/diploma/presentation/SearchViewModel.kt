package ru.practicum.android.diploma.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.VacancyState
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(private val vacancyInteractor: VacancyInteractor) : ViewModel() {
    private val vacancyLiveData = MutableLiveData<VacancyState>()
    fun observeVacancy(): LiveData<VacancyState> = vacancyLiveData

    private val currentPage = 1
    private var searchJob: Job? = null
    private val vacancySearchDebounce = debounce<String>(DEBOUNCE_DELAY, viewModelScope, true) { text->
        search(text)
    }

    fun searchDebounce(text: String) {
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
}
