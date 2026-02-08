package ru.practicum.android.diploma.ui.filtration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IndustryInteractor
import ru.practicum.android.diploma.domain.models.IndustryState

class FiltrationSettingsViewModel(
    private val industryInteractor: IndustryInteractor
) : ViewModel() {

    private val industryLiveData = MutableLiveData<IndustryState>()
    fun observeIndustries(): LiveData<IndustryState> = industryLiveData

    fun loadIndustries() {
        viewModelScope.launch {
            industryLiveData.postValue(IndustryState.Loading)
            industryLiveData.postValue(industryInteractor.getIndustries())
        }
    }
}
