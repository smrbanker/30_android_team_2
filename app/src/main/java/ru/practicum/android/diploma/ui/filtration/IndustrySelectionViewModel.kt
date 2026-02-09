package ru.practicum.android.diploma.ui.filtration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IndustryInteractor
import ru.practicum.android.diploma.domain.models.IndustryState

class IndustrySelectionViewModel(
    private val industryInteractor: IndustryInteractor
) : ViewModel() {

    private val industryLiveData = MutableLiveData<IndustryState>()
    fun observeIndustries(): LiveData<IndustryState> = industryLiveData

    fun loadIndustries() {
        viewModelScope.launch {
            industryLiveData.postValue(IndustryState.Loading)
            Log.d("IndustrySelectionViewModel", "Загрузка отраслей начата")

            try {
                val result = industryInteractor.getIndustries()
                Log.d("IndustrySelectionViewModel", "Получены отрасли: $result")
                industryLiveData.postValue(result)
            } catch (e: Exception) {
                Log.e("IndustrySelectionViewModel", "Ошибка при загрузке отраслей: ${e.message}", e)
                industryLiveData.postValue(IndustryState.Error(e.message ?: "Unknown error"))
            }
        }
    }
}
