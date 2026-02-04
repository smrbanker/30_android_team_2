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

class VacancyViewModel(
    private val favouritesInteractor: FavouritesInteractor,
    private val vacancyInteractor: SearchVacancyDetailsInteractor,
    private val context: Context
) : ViewModel() {

    // region LiveData
    val favouriteInfo = MutableLiveData<Boolean>()
    fun observeFavouriteInfo(): LiveData<Boolean> = favouriteInfo

    private val stateLiveData = MutableLiveData<VacancyDetailsState>()
    fun observeState(): LiveData<VacancyDetailsState> = stateLiveData
    // endregion

    // region Методы получения объекта вакансии
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
                                errorMessage = context.getString(R.string.server_error),
                            )
                        )
                    }

                    items.isEmpty() -> {
                        renderState(
                            VacancyDetailsState.Empty(
                                emptyMessage = context.getString(R.string.vacancy_not_found_or_deleted)
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
    // endregion

    // region Rendering
    private fun renderState(state: VacancyDetailsState) {
        stateLiveData.postValue(state)
    }
    private fun renderFavorite(favourite: Boolean) {
        favouriteInfo.postValue(favourite)
    }
    // endregion

    // region Методы конвертации и обработки данных объекта Vacancy
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
                this += VacancyCastItem.BigHeaderItem(context.getString(R.string.vacancy_description))
                this.addAll(getDescriptionItemsList(vacancy.description))
            }
            if (vacancy.skills.isNotEmpty()) {
                this += VacancyCastItem.BigHeaderItem(context.getString(R.string.key_skills))
                this += VacancyCastItem.SkillItem(vacancy.skills)
            }
            // if (!vacancy.name.isNullOrEmpty()) {
            //    this += VacancyCastItem.BigHeaderItem(context.getString(R.string.contacts))
            //    if (!vacancy.email.isNullOrEmpty()) {
            //        this += VacancyCastItem.MailItem(vacancy.name, vacancy.email)
            //    } else {
            //        this += VacancyCastItem.MailItem(vacancy.name, "")
            //    }
            //}
            if (!vacancy.phone.isNullOrEmpty()) {
                val phones = vacancy.phone.split(',')
                this += phones.map { VacancyCastItem.PhoneItem(it) }
            }
        }
        return items
    }
    // endregion

    // region Методы проверки состояния
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
    // endregion

    companion object {
        private const val SQL_EXCEPTION = "SQLException"
        const val DB_ERROR_INSERT = "Ошибка сохранения данных в БД"
        const val DB_ERROR_DELETE = "Ошибка удаления данных из БД"
        const val DB_ERROR_CHECK = "Ошибка проверки данных в БД"
    }
}
