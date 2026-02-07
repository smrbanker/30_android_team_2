package ru.practicum.android.diploma.ui.filtration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.practicum.android.diploma.domain.api.FilterSpInteractor
import ru.practicum.android.diploma.domain.models.Filter
import java.io.IOException

class FiltrationSettingsViewModel(
    private val filterInteractor: FilterSpInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FiltrationSettingsState>()
    fun observeState(): LiveData<FiltrationSettingsState> = stateLiveData

    fun showFilter() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                processResult(filterInteractor.output())
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(FiltrationSettingsState.Error(SP_ERROR_OUTPUT))
            }
        }
    }

    fun saveFilter(f: Filter) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                filterInteractor.input(f)
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(FiltrationSettingsState.Error(SP_ERROR_INPUT))
            }
        }
    }

    fun clearFilter() {
        runBlocking {
            try {
                filterInteractor.clearAll()
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(FiltrationSettingsState.Error(SP_ERROR_CLEAR))
            }
        }
    }

    fun clearRegion() {
        runBlocking {
            try {
                filterInteractor.clearRegion()
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(FiltrationSettingsState.Error(SP_ERROR_CLEAR))
            }
        }
    }

    fun clearIndustry() {
        runBlocking {
            try {
                filterInteractor.clearIndustry()
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(FiltrationSettingsState.Error(SP_ERROR_CLEAR))
            }
        }
    }

    fun clearSalary() {
        runBlocking {
            try {
                filterInteractor.clearSalary()
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(FiltrationSettingsState.Error(SP_ERROR_CLEAR))
            }
        }
    }

    fun onlyWithSalary() {
        runBlocking {
            try {
                filterInteractor.onlyWithSalary()
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(FiltrationSettingsState.Error(SP_ERROR_INPUT))
            }
        }
    }

    fun setSalary(value: String?) {
        runBlocking {
            try {
                filterInteractor.setSalary(value)
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(FiltrationSettingsState.Error(SP_ERROR_INPUT))
            }
        }
    }

    fun setStatus(status: Boolean) {
        runBlocking {
            try {
                filterInteractor.setStatus(status)
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(FiltrationSettingsState.Error(SP_ERROR_INPUT))
            }
        }
    }


    private fun processResult(filter: Filter) {
        renderState(FiltrationSettingsState.Content(filter))
    }

    private fun renderState(state: FiltrationSettingsState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SP_EXCEPTION = "SPException"
        const val SP_ERROR_INPUT = "Ошибка сохранения данных в SP"
        const val SP_ERROR_OUTPUT = "Ошибка чтения данных из SP"
        const val SP_ERROR_CLEAR = "Ошибка сохранения пустых данных в SP"
    }
}
