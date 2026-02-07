package ru.practicum.android.diploma.ui.favorites.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.db.FavouritesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import java.sql.SQLException

class FavouritesViewModel(
    private val favouritesInteractor: FavouritesInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavouritesState>(FavouritesState.Loading)
    fun observeState(): LiveData<FavouritesState> = stateLiveData

    fun fillData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                favouritesInteractor
                    .getAllFavoriteVacancy()
                    .collect { vacancies ->
                        processResult(vacancies)
                    }
            } catch (e: SQLException) {
                Log.e("SQLException", e.toString())
                stateLiveData.postValue(FavouritesState.Error)
            }
        }
    }

    private fun processResult(vacancies: List<Vacancy>) {
        if (vacancies.isEmpty()) {
            renderState(FavouritesState.Empty)
        } else {
            renderState(FavouritesState.Content(vacancies))
        }
    }

    private fun renderState(state: FavouritesState) {
        stateLiveData.postValue(state)
    }
}
