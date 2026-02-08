package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.AreasInteractor
import ru.practicum.android.diploma.domain.api.FilterSpInteractor
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Location

class FiltrationCountryViewModel(
    private val areasInteractor: AreasInteractor,
    private val prefsInteractor: FilterSpInteractor
    ) : ViewModel() {
    init {
        setupUi()
    }
    private val countriesLiveData = MutableLiveData<Pair<List<Country>?, String?>>()
    fun observeCountries(): LiveData<Pair<List<Country>?, String?>> = countriesLiveData

    private fun setupUi() {
        viewModelScope.launch {
            countriesLiveData.postValue(areasInteractor.getCountries())
        }
    }

    fun saveCountry(country: Country) {
        val filter = prefsInteractor.output()
        prefsInteractor.input(Filter(
            location = Location(country = country, region = null),
            sector = filter.sector,
            salary = filter.salary,
            onlyWithSalary = filter.onlyWithSalary
        ))
    }
}
