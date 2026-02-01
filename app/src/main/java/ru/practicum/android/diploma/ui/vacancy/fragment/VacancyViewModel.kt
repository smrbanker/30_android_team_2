package ru.practicum.android.diploma.ui.vacancy.fragment

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

class VacancyViewModel( // В ТЕЛЕ КЛАССА ВЕСЬ КОД МОЙ, ОН НУЖЕН ДЛЯ ДОБАВЛЕНИЯ ВАКАНСИИ В ИЗБРАННОЕ
    private val favouritesInteractor: FavouritesInteractor
) : ViewModel() {

    val favouriteInfo = MutableLiveData<Boolean>()
    fun observeFavouriteInfo(): LiveData<Boolean> = favouriteInfo

    fun changeFavourite(vacancy: Vacancy) {
        viewModelScope.launch {
            val favourite = favouriteInfo.value ?: false
            if (favourite) {
                try {
                    favouritesInteractor.deleteFavoriteVacancy(vacancy)
                } catch (e: SQLException) {
                    Log.e("SQLException", e.toString())
                    // stateLiveData.postValue(VacancyDetailState.Error(e.toString())) // РАЗКОММЕНТИРУЙ, КАК ДОБАВИШЬ STATES
                }
            } else {
                try {
                    favouritesInteractor.insertNewFavoriteVacancy(vacancy)
                } catch (e: SQLException) {
                    Log.e("SQLException", e.toString())
                    // stateLiveData.postValue(VacancyDetailState.Error(e.toString())) // РАЗКОММЕНТИРУЙ, КАК ДОБАВИШЬ STATES
                }
            }
            renderFavorite(!favourite)
        }
    }

    fun checkFavourite(vacancyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // ВОТ ЗДЕСЬ ВАЖНЫЙ МОМЕНТ. У НАС РЕАЛИЗОВАН ПОИСК ВАКАНСИИ ПО ID. Я ПОКА НЕ МОГУ ПРОВЕРИТЬ
                // БУДЕТ ЛИ РАБОТАТЬ КОД НИЖЕ. ЕСЛИ НЕ БУДЕТ, ТО ЕСТЬ ВАРИАНТ ЗАМЕНИТЬ ВАКАНСИЮ НА ЕЕ ID,
                // ЭТОТ ВАРИАНТ ТОЧНО РАБОЧИЙ. ПОКА НЕ УВЕРЕН, ЧТО ПРИ ОТСУТСТВИИ ВАКАНСИИ ТАК БУДЕТ РАБОТАТЬ
                // ДМИТРИЙ, КАК ПРОВЕРИШЬ НАПИШИ - ЕСЛИ НЕ ОК, ТО Я ЗАМЕНЮ ВСЕ НА ВОЗВРАТ ID ВМЕСТО ВСЕЙ ВАКАНСИИ
                val vacancy = favouritesInteractor.getFavoriteVacancy(vacancyId)
                if (vacancyId == vacancy.id) {
                    renderFavorite(true)
                } else {
                    renderFavorite(false)
                }
            } catch (e: SQLException) {
                Log.e("SQLException", e.toString())
                // stateLiveData.postValue(VacancyDetailState.Error(e.toString())) // РАЗКОММЕНТИРУЙ, КАК ДОБАВИШЬ STATES
            }
        }
    }

    private fun renderFavorite(favourite: Boolean) {
        favouriteInfo.postValue(favourite)
    }
}
