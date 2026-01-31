package ru.practicum.android.diploma.data.dto.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyConverter(private val gson: Gson) {
    fun convert(vacancyDto: VacancyDto): Vacancy = with(vacancyDto) {
        Vacancy(
            id = id,
            name = name,
            description = description,
            salaryFrom = salary?.from,
            salaryTo = salary?.to,
            currency = salary?.currency,
            city = address?.city,
            street = address?.street,
            building = address?.building,
            fullAddress = address?.fullAddress,
            experience = experience?.name,
            schedule = schedule?.name,
            employment = employment?.name,
            contact = contacts?.name,
            email = contacts?.email,
            phone = convertStringListToString(contacts?.phone),
            employer = employer.name,
            logo = employer.logo,
            area = area.name,
            skills = convertStringListToString(skills),
            url = url,
            industry = industry.name
        )
    }

    private fun convertStringListToString(stringList: List<String>?): String {
        return if (stringList != null) {
            gson.toJson(stringList, object : TypeToken<List<String>>(){}.type)
        } else {
            ""
        }
    }
}
