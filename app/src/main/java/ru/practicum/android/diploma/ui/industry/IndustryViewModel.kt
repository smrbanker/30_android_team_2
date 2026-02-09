package ru.practicum.android.diploma.ui.industry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.practicum.android.diploma.domain.api.FilterSpInteractor
import ru.practicum.android.diploma.domain.api.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Sector
import java.io.IOException

class IndustryViewModel(
    private val filterInteractor: FilterSpInteractor,
    private val industryInteractor: IndustryInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<IndustryState>(IndustryState.Loading)
    fun observeState(): LiveData<IndustryState> = stateLiveData

    private var originalList: MutableList<Sector> = ArrayList()
    private val filteredList: MutableList<Sector> = ArrayList()

    private var latestSearchText: String? = null

    var sector: Sector? = null
    var filter: Filter? = null

    fun fillData() {
        stateLiveData.postValue(IndustryState.Loading)
        runBlocking { // }viewModelScope.launch {
            val ind = industryInteractor.getIndustries()

            if (ind.second != null) {
                stateLiveData.postValue(IndustryState.Error(API_ERROR_OUTPUT))
            } else {
                originalList = ind.first?.map { Sector(it.id, it.name, false) }!!.toMutableList()
            }
        }
        originalList = initOriginalList(originalList)
        postSectorsList()
    }

    fun setIndustry() {
        var industry: Sector? = null

        for (i in 0..<filteredList.size) {
            if (filteredList[i].isChecked) {
                industry = Sector(filteredList[i].id, filteredList[i].name, true)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                filterInteractor.setIndustry(industry)
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(IndustryState.Error(SP_ERROR_INPUT))
            }
        }
    }

    fun initOriginalList(list: MutableList<Sector>): MutableList<Sector> {
        val f = getInitFilter()
        if (f != null) {
            for (i in 0..<list.size) {
                if (list[i].id == f.sector?.id) {
                    val id = list[i].id
                    val name = list[i].name
                    list.remove(list[i])
                    list.add(i, Sector(id, name, true))

                } else {
                    val id = list[i].id
                    val name = list[i].name
                    list.remove(list[i])
                    list.add(i, Sector(id, name, false))
                }
            }
        }

        return list
    }

    fun getInitFilter(): Filter? {
        runBlocking { // viewModelScope.launch(Dispatchers.IO) {
            try {
                filter = filterInteractor.output()
            } catch (e: IOException) {
                Log.e(SP_EXCEPTION, e.toString())
                stateLiveData.postValue(IndustryState.Error(SP_ERROR_OUTPUT))
            }
        }
        return filter
    }

    fun changeItem(industry: Sector) {
        sector = industry
        postSectorsList()
    }

    fun search(searchText: String?) {
        latestSearchText = searchText
        postSectorsList()
    }

    private fun postSectorsList() {
        filteredList.clear()
        if (latestSearchText.isNullOrEmpty()) {
            filteredList.addAll(originalList)
        }
        if (latestSearchText?.isNotBlank() == true) {
            for (item in originalList) {
                if (item.name.contains(latestSearchText!!, true)) {
                    filteredList.add(item)
                }
            }
        }

        sectorWork()

        var flag = false
        for (i in 0..<filteredList.size) {
            if (filteredList[i].isChecked) {
                flag = true
            }
        }

        processResult(filteredList.sortedBy { it.name }, flag)
    }

    private fun sectorWork() {
        sector?.let { sector ->
            val indexChecked = filteredList.indexOfFirst { it.id == sector.id }
            indexChecked(indexChecked)
        }
    }

    private fun indexChecked(index: Int) {
        if (index >= 0) {
            for (i in 0..<filteredList.size) {
                if (i == index) {
                    val item = filteredList[i]
                    filteredList[i] = item.copy(isChecked = true)
                } else {
                    val item = filteredList[i]
                    filteredList[i] = item.copy(isChecked = false)
                }
            }
        }
    }

    private fun processResult(industries: List<Sector>, flag: Boolean) {
        renderState(IndustryState.Content(industries, flag))
    }

    private fun renderState(state: IndustryState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SP_EXCEPTION = "SPException"
        const val SP_ERROR_INPUT = "Ошибка сохранения данных в SP"
        const val SP_ERROR_OUTPUT = "Ошибка чтения данных из SP"
        const val API_ERROR_OUTPUT = "Ошибка чтения данных из API"
        }
    }
}
