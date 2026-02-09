package ru.practicum.android.diploma.ui.filtration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IndustryInteractor
import ru.practicum.android.diploma.domain.models.IndustryState
import java.io.IOException

class IndustrySelectionViewModel(
    private val industryInteractor: IndustryInteractor
) : ViewModel() {

    private val industryLiveData = MutableLiveData<IndustryState>()
    fun observeIndustries(): LiveData<IndustryState> = industryLiveData

    object IndustryConstants {
        const val TAG_VIEW_MODEL = "IndustrySelectionViewModel"
        const val MESSAGE_LOADING_STARTED = "Загрузка отраслей начата"
        const val MESSAGE_SUCCESS = "Получены отрасли: "
        const val MESSAGE_ERROR_NETWORK = "Ошибка сети: "
        const val MESSAGE_ERROR_HTTP = "Ошибка HTTP: "
        const val MESSAGE_UNKNOWN_ERROR = "Неизвестная ошибка"
    }

    fun loadIndustries() {
        viewModelScope.launch {
            industryLiveData.postValue(IndustryState.Loading)
            Log.d(
                IndustryConstants.TAG_VIEW_MODEL,
                IndustryConstants.MESSAGE_LOADING_STARTED
            )

            try {
                val result = industryInteractor.getIndustries()
                Log.d(
                    IndustryConstants.TAG_VIEW_MODEL,
                    "${IndustryConstants.MESSAGE_SUCCESS}$result"
                )
                industryLiveData.postValue(result)
            } catch (e: IOException) {
                Log.e(
                    IndustryConstants.TAG_VIEW_MODEL,
                    "${IndustryConstants.MESSAGE_ERROR_NETWORK}${e.message}",
                    e
                )
                industryLiveData.postValue(
                    IndustryState.Error("${IndustryConstants.MESSAGE_ERROR_NETWORK}${e.message}")
                )
            } catch (e: HttpException) {
                Log.e(
                    IndustryConstants.TAG_VIEW_MODEL,
                    "${IndustryConstants.MESSAGE_ERROR_HTTP}${e.message}",
                    e
                )
                industryLiveData.postValue(
                    IndustryState.Error("${IndustryConstants.MESSAGE_ERROR_HTTP}${e.message}")
                )
            }
        }
    }
}
