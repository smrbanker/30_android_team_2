package ru.practicum.android.diploma.ui.filtration

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterSpInteractor
import ru.practicum.android.diploma.domain.models.Filter

class FiltrationSettingsViewModel(
    private val filterInteractor: FilterSpInteractor,
) : ViewModel() {

    fun showFilter() {
        val filter: Filter = filterInteractor.output()
        Log.d("OUTPUT", filter.toString())
    }

    fun saveFilter(f: Filter) {
        filterInteractor.input(f)
    }
}
