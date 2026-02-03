package ru.practicum.android.diploma.ui.vacancy.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.SearchVacancyDetailsInteractor
import ru.practicum.android.diploma.domain.db.FavouritesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.util.salaryFormatter
import java.sql.SQLException

class VacancyViewModel( // В ТЕЛЕ КЛАССА ВЕСЬ КОД МОЙ, ОН НУЖЕН ДЛЯ ДОБАВЛЕНИЯ ВАКАНСИИ В ИЗБРАННОЕ
    private val favouritesInteractor: FavouritesInteractor,
    private val vacancyInteractor: SearchVacancyDetailsInteractor,
    private val context: Context
) : ViewModel() {

    val favouriteInfo = MutableLiveData<Boolean>()
    fun observeFavouriteInfo(): LiveData<Boolean> = favouriteInfo

    private val stateLiveData = MutableLiveData<VacancyDetailsState>()
    fun observeState(): LiveData<VacancyDetailsState> = stateLiveData

    fun searchVacancyId(id: String) {
        if (!id.isNullOrEmpty()) {
            renderState(
                VacancyDetailsState.Loading
            )
            viewModelScope.launch {
                val result = vacancyInteractor.searchVacancyDetails(id)
                val items = mutableListOf<VacancyCastItem>()
                val vacancy: Vacancy? = result.data
                if (result.data != null) {
                    items.addAll(buildVacancyCastItemList(result.data))
                }
                when {
                    result.message != null -> {
                        renderState(
                            VacancyDetailsState.Error(
                                errorMessage = R.string.server_error.toString(),
                            )
                        )
                    }

                    items.isEmpty() -> {
                        renderState(
                            VacancyDetailsState.Empty(
                                emptyMessage = R.string.vacancy_not_found_or_deleted.toString()
                            )
                        )
                    }

                    else -> {
                        renderState(
                            VacancyDetailsState.Content(
                                vacancy = items,
                                vacancyFull = vacancy
                            )
                        )
                    }
                }

            }
        }
    }
    fun setVacancyFromBase(vacancy: Vacancy) {
        val items = mutableListOf<VacancyCastItem>()
        if (vacancy != null) {
            items.addAll(buildVacancyCastItemList(vacancy))
        }
        renderState(
            VacancyDetailsState.Content(
                vacancy = items,
                vacancyFull = vacancy
            )
        )
    }

    private fun renderState(state: VacancyDetailsState) {
        stateLiveData.postValue(state)
    }

    private fun getDescriptionItemsList(description: String): List<VacancyCastItem> {
        val descriptionList = description.split("\n") as MutableList<String>
        descriptionList.removeIf { it.isEmpty() }
        val items = mutableListOf<VacancyCastItem>()
        descriptionList.forEach { item ->
            if (item.contains(':')) {
                items.add(VacancyCastItem.SmallHeaderItem(item.trim()))
            } else {
                items.add(VacancyCastItem.Item(item.trim()))
            }
        }
        return items
    }

    private fun buildVacancyCastItemList(vacancy: Vacancy): List<VacancyCastItem> {
        val items = buildList<VacancyCastItem>() {
            if (vacancy.name.isNotEmpty()) {
                val salary = salaryFormatter(vacancy, context)
                this += VacancyCastItem.GeneralHeaderItem(
                    vacancyTitle = vacancy.name,
                    vacancySalary = salary)
            }
            if (vacancy.employer.isNotEmpty()
                && vacancy.area.isNotEmpty()
                && vacancy.logo.isNotEmpty()) {
                this += VacancyCastItem.CompanyItem(
                    employer = vacancy.employer,
                    area = vacancy.area,
                    logo = vacancy.logo)
            }
            if (vacancy.description.isNotEmpty()) {
                this += VacancyCastItem.BigHeaderItem("Описание вакансии")
                this.addAll(getDescriptionItemsList(vacancy.description))
            }
            if (vacancy.skills.isNotEmpty()) {
                this += VacancyCastItem.BigHeaderItem("Ключевые навыки")
                val skillsList = vacancy.skills.split(',')
                this += skillsList.map { VacancyCastItem.Item(it) }
            }
        }
        return items
    }

    fun changeFavourite(vacancy: Vacancy?) {
        if (vacancy != null) {
            viewModelScope.launch {
                val favourite = favouriteInfo.value ?: false
                if (favourite) {
                    try {
                        favouritesInteractor.deleteFavoriteVacancy(vacancy)
                    } catch (e: SQLException) {
                        Log.e(SQL_EXCEPTION, e.toString())
                        stateLiveData.postValue(VacancyDetailsState.ErrorDB(DB_ERROR_DELETE))
                    }
                } else {
                    try {
                        favouritesInteractor.insertNewFavoriteVacancy(vacancy)
                    } catch (e: SQLException) {
                        Log.e(SQL_EXCEPTION, e.toString())
                        stateLiveData.postValue(VacancyDetailsState.ErrorDB(DB_ERROR_INSERT))
                    }
                }
                renderFavorite(!favourite)
            }
        }
    }

    fun checkFavourite(vacancyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val vacancy = favouritesInteractor.getFavoriteVacancy(vacancyId)
                if (vacancy == null) {
                    renderFavorite(false)
                } else {
                    renderFavorite(true)
                }
            } catch (e: SQLException) {
                Log.e(SQL_EXCEPTION, e.toString())
                stateLiveData.postValue(VacancyDetailsState.ErrorDB(DB_ERROR_CHECK))
            }
        }
    }

    private fun renderFavorite(favourite: Boolean) {
        favouriteInfo.postValue(favourite)
    }

    companion object {
        private const val SQL_EXCEPTION = "SQLException"
        const val DB_ERROR_INSERT = "Ошибка сохранения данных в БД"
        const val DB_ERROR_DELETE = "Ошибка удаления данных из БД"
        const val DB_ERROR_CHECK = "Ошибка проверки данных в БД"
    }
}
