package ru.practicum.android.diploma.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import ru.practicum.android.diploma.domain.api.FilterSpInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Location
import java.io.IOException

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

    fun fetchCountryAndRegion() {
        val filter = prefsInteractor.output()
        countryLiveData.postValue(filter.location.country?.name)
        regionLiveData.postValue(filter.location.region?.name)
    }

    fun clearCountryAndRegion() {
        runBlocking(Dispatchers.IO) {
            try {
                prefsInteractor.clearRegion()
                fetchCountryAndRegion()
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
            }
        }
    }

    fun clearRegion() {
        runBlocking(Dispatchers.IO) {
            val filter = prefsInteractor.output()
            prefsInteractor.input(
                Filter(
                    location = Location(country = filter.location.country, region = null),
                    sector = filter.sector,
                    salary = filter.salary,
                    onlyWithSalary = filter.onlyWithSalary
                )
            )
            fetchRegion()
        }
    }

    companion object {
        private const val SP_EXCEPTION = "SPException"
    }
}
