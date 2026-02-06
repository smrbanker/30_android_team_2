package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.FilterState

class FilterRepositoryImpl : FilterRepository {
    // Эти переменные хранят состояние основного экрана с фильтрами, они выступают в роли контейнера
    // Значения в эти переменные помещаются из префсов
    private val _filterAreaState = MutableStateFlow<FilterState.Area?>(null)
    private val _filterIndustryState = MutableStateFlow<FilterState.Industry?>(null)
    private val _filterSalaryState = MutableStateFlow<FilterState.Salary?>(null)
    private val _filterOnlyWithSalaryState = MutableStateFlow<FilterState.OnlyWithSalary?>(null)

    // Эти переопределенные переменные являются потоками. На них можно подписаться (observe)
    // Функция asStateFlow() делает из переменных выше поток
    override val filterAreaState: Flow<FilterState.Area?>
        get() = _filterAreaState.asStateFlow()
    override val filterIndustryState: Flow<FilterState.Industry?>
        get() = _filterIndustryState.asStateFlow()
    override val filterSalaryState: Flow<FilterState.Salary?>
        get() = _filterSalaryState.asStateFlow()
    override val filterOnlyWithSalaryState: Flow<FilterState.OnlyWithSalary?>
        get() = _filterOnlyWithSalaryState.asStateFlow()

    // Функция обновляет значение в переменной _filterAreaState
    override fun updateArea(areaId: String, areaName: String, regionId: String, regionName: String) {
        _filterAreaState.tryEmit(
            FilterState.Area(
                areaId = areaId,
                areaName = areaName,
                regionId = regionId,
                regionName = regionName
            )
        )
    }

    // Функция обновляет значение в переменной _filterIndustryState
    override fun updateIndustry(industryId: String, industryName: String) {
        _filterIndustryState.tryEmit(
            FilterState.Industry(
                industryId = industryId,
                industryName = industryName
            )
        )
    }

    // Функция обновляет значение в переменной _filterSalaryState
    override fun updateSalary(salary: Int) {
        _filterSalaryState.tryEmit(FilterState.Salary(salary = salary))
    }

    // Функция обновляет значение в переменной _filterOnlyWithSalaryState
    override fun updateOnlyWithSalary(onlyWithSalary: Boolean) {
        _filterOnlyWithSalaryState.tryEmit(
            FilterState.OnlyWithSalary(onlyWithSalary = onlyWithSalary)
        )
    }

    // Функция используется на экране фильтров при нажатии на кнопку "Сбросить"
    override fun clearFilters() {
        _filterAreaState.tryEmit(null)
        _filterIndustryState.tryEmit(null)
        _filterSalaryState.tryEmit(null)
        _filterOnlyWithSalaryState.tryEmit(null)
    }
}
