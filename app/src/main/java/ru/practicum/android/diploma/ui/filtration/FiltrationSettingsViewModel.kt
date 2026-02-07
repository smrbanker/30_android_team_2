package ru.practicum.android.diploma.ui.filtration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                Log.d("FILTER", filterInteractor.output().toString())
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                filterInteractor.clear()
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(FiltrationSettingsState.Error(SP_ERROR_CLEAR))
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
