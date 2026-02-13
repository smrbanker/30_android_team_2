package ru.practicum.android.diploma.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.api.FilterSpRepository
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Location
import ru.practicum.android.diploma.domain.models.Sector

class FilterSpRepositoryImpl(
    private val sp: SharedPreferences,
    private val gson: Gson,
) : FilterSpRepository {

    override fun input(filter: Filter) {
        val json = gson.toJson(filter, Filter::class.java)
        sp.edit {
            putString(FILTER_SP_KEY, json)
        }
    }

    override fun output(): Filter {
        val json = sp.getString(FILTER_SP_KEY, null) ?: return Filter()
        return gson.fromJson(json, Filter::class.java)
    }

    override fun clearAll() {
        val f = Filter(
            location = Location(null, null),
            sector = null,
            salary = null,
            onlyWithSalary = false
        )
        input(f)
    }

    override fun clearRegion() {
        var f = output()
        f = Filter(
            location = Location(null, null),
            sector = f.sector,
            salary = f.salary,
            onlyWithSalary = f.onlyWithSalary
        )
        input(f)
    }

    override fun clearIndustry() {
        var f = output()
        f = Filter(
            location = f.location,
            sector = null,
            salary = f.salary,
            onlyWithSalary = f.onlyWithSalary
        )
        input(f)
    }

    override fun clearSalary() {
        var f = output()
        f = Filter(
            location = f.location,
            sector = f.sector,
            salary = null,
            onlyWithSalary = f.onlyWithSalary
        )
        input(f)
    }

    override fun onlyWithSalary() {
        var f = output()
        f = Filter(
            location = f.location,
            sector = f.sector,
            salary = f.salary,
            onlyWithSalary = !f.onlyWithSalary
        )
        input(f)
    }

    override fun setSalary(value: String?) {
        var f = output()
        val amount = value?.toInt()
        f = Filter(
            location = f.location,
            sector = f.sector,
            salary = amount,
            onlyWithSalary = f.onlyWithSalary
        )
        input(f)
    }

    override fun setStatus(status: Boolean) {
        var f = output()
        f = Filter(
            location = f.location,
            sector = f.sector,
            salary = f.salary,
            onlyWithSalary = status
        )
        input(f)
    }

    override fun setIndustry(industry: Sector?) {
        var f = output()
        f = Filter(
            location = f.location,
            sector = industry,
            salary = f.salary,
            onlyWithSalary = f.onlyWithSalary
        )
        input(f)
    }

    private companion object {
        const val FILTER_SP_KEY = "filter"
    }
}
