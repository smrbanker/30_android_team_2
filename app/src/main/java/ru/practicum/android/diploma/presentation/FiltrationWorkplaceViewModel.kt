package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterSpInteractor

class FiltrationWorkplaceViewModel(private val prefsInteractor: FilterSpInteractor) : ViewModel() {
    private val countryLiveData = MutableLiveData<String>()
    fun observeCountry(): LiveData<String> = countryLiveData

    private val regionLiveData = MutableLiveData<String>()
    fun observeRegion(): LiveData<String> = regionLiveData

    fun fetchCountry() {
        val filter = prefsInteractor.output()
        countryLiveData.postValue(filter.location.country?.name)
    }

    fun fetchRegion() {
        val filter = prefsInteractor.output()
        regionLiveData.postValue(filter.location.region?.name)
    }
}
