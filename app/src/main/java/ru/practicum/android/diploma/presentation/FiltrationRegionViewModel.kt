package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.practicum.android.diploma.domain.api.AreasInteractor
import ru.practicum.android.diploma.domain.api.FilterSpInteractor
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Location
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.RegionState
import ru.practicum.android.diploma.util.debounce

class FiltrationRegionViewModel(
    private val areaInteractor: AreasInteractor,
    private val prefsInteractor: FilterSpInteractor
) : ViewModel() {
    val regionStateLiveData = MutableLiveData<RegionState>()
    fun observeRegions(): LiveData<RegionState> = regionStateLiveData
    val regionList = mutableListOf<Region>()
    private var latestSearchText = ""
    private var searchJob: Job? = null
    private val regionSearchDebounce = debounce<String>(
        DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { text ->
        searchInList()
    }

    fun searchDebounce(text: String) {
        if (latestSearchText == text) return
        latestSearchText = text
        regionSearchDebounce(text)
    }

    private fun searchInList() {
        if (latestSearchText.isNotEmpty()) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                val result = mutableListOf<Region>()
                regionList.forEach { region ->
                    if (region.name.contains(latestSearchText)) {
                        result.add(region)
                    }
                }
                when {
                    result.isEmpty() -> {
                        renderState(RegionState.Empty)
                    }
                    else -> {
                        renderState(RegionState.Content(result))
                    }
                }
            }
        }
    }
    fun searchCountryRegions(parentName: String) {
        searchJob?.cancel()
        if (!parentName.isNullOrEmpty()) {
            renderState(
                RegionState.Loading
            )
            searchJob = viewModelScope.launch {
                val result = areaInteractor.getCountryRegions(parentName)
                regionList.clear()
                if (result.first != null) {
                    regionList.addAll(result.first!!)
                }
                when {
                    result.second != null -> {
                        renderState(
                            RegionState.Error(errorMessage = result.second!!)
                        )
                    }

                    else -> {
                        renderState(
                            RegionState.Content(regionList)
                        )
                    }
                }
            }
        }
    }

    fun searchAllRegions() {
        searchJob?.cancel()
        renderState(
            RegionState.Loading
        )
        searchJob = viewModelScope.launch {
            val result = areaInteractor.getAllRegions()
            regionList.clear()
            if (result.first != null) {
                regionList.addAll(result.first!!)
            }
            when {
                result.second != null -> {
                    renderState(RegionState.Error(errorMessage = result.second!!))
                }

                else -> {
                    renderState(RegionState.Content(regionList))
                }
            }
        }
    }

    fun saveRegion(region: Region, isCountrySelected: Boolean) {
        runBlocking(Dispatchers.IO) {
            val filter = prefsInteractor.output()
            if (!isCountrySelected) {
                prefsInteractor.input(
                    Filter(
                        location = Location(country = Country(region.parentId, region.parentName), region = region),
                        sector = filter.sector,
                        salary = filter.salary,
                        onlyWithSalary = filter.onlyWithSalary
                    )
                )
            } else {
                prefsInteractor.input(
                    Filter(
                        location = Location(country = filter.location.country, region = region),
                        sector = filter.sector,
                        salary = filter.salary,
                        onlyWithSalary = filter.onlyWithSalary
                    )
                )
            }
        }
    }
    private fun renderState(state: RegionState) {
        regionStateLiveData.postValue(state)
    }
    fun getSavedRegionList() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            when {
                regionList.isEmpty() -> {
                    renderState(RegionState.Empty)
                }
                else -> {
                    renderState(RegionState.Content(regionList))
                }
            }
        }
    }
    companion object {
        private const val DEBOUNCE_DELAY = 2000L
    }
}
