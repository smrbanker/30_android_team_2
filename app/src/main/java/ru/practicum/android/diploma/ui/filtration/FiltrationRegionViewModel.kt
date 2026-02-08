package ru.practicum.android.diploma.ui.filtration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.AreasInteractor
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.RegionState

class FiltrationRegionViewModel(val areaInteractor: AreasInteractor): ViewModel(){
    val regionStateLiveData = MutableLiveData<RegionState>()
    fun observeFavouriteInfo(): LiveData<RegionState> = regionStateLiveData
    val regionList = mutableListOf<Region>()

    fun searchCountryRegions(parentId: String) {
        if (!parentId.isNullOrEmpty()) {
            renderState(
                RegionState.Loading
            )
            viewModelScope.launch {
                val result = areaInteractor.getCountryRegions(parentId.toInt())
                regionList.clear()
                if (result.first != null) {
                    regionList.addAll(result.first!!)
                }
                when {
                    result.second != null -> {
                        renderState(
                            RegionState.Error(
                                errorMessage = result.second!!,
                            )
                        )
                    }

                    regionList.isEmpty() -> {
                        renderState(
                            RegionState.Empty(
                                emptyMessage = R.string.region_is_not_found
                            )
                        )
                    }

                    else -> {
                        renderState(
                            RegionState.Content(
                                regionList
                            )
                        )
                    }
                }

            }
        }
    }

    fun searchAllRegions() {
        renderState(
            RegionState.Loading
        )
        viewModelScope.launch {
            val result = areaInteractor.getAllRegions()
            regionList.clear()
            if (result.first != null) {
                regionList.addAll(result.first!!)
            }
            when {
                result.second != null -> {
                    renderState(
                        RegionState.Error(
                            errorMessage = result.second!!,
                        )
                    )
                }

                regionList.isEmpty() -> {
                    renderState(
                        RegionState.Empty(
                            emptyMessage = R.string.region_is_not_found
                        )
                    )
                }

                else -> {
                    renderState(
                        RegionState.Content(
                            regionList
                        )
                    )
                }
            }

        }

    }

    private fun renderState(state: RegionState) {
        regionStateLiveData.postValue(state)
    }
}
