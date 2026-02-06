package ru.practicum.android.diploma.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.api.FilterSpRepository
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Location

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

    override fun clear() {
        val f = Filter(
            location = Location(null, null),
            sector = null,
            salary = null,
            onlyWithSalary = false
        )
        input(f)
    }

    private companion object {
        const val FILTER_SP_KEY = "filter"
    }
}
