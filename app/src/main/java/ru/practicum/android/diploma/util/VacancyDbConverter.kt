package ru.practicum.android.diploma.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyDbConverter {

    fun map(vacancy: Vacancy): VacancyDetailEntity {
        return VacancyDetailEntity(
            vacancy.id,
            vacancy.name,
            vacancy.description,
            vacancy.salaryFrom,
            vacancy.salaryTo,
            vacancy.currency,
            vacancy.city,
            vacancy.street,
            vacancy.building,
            vacancy.fullAddress,
            vacancy.experience,
            vacancy.schedule,
            vacancy.employment,
            vacancy.contact,
            vacancy.email,
            phoneListToJson(vacancy.phone),
            vacancy.employer,
            vacancy.logo,
            vacancy.area,
            vacancy.skills,
            vacancy.url,
            vacancy.industry
        )
    }

    fun map(vacancy: VacancyDetailEntity): Vacancy {
        return Vacancy(
            vacancy.id,
            vacancy.name,
            vacancy.description,
            vacancy.salaryFrom,
            vacancy.salaryTo,
            vacancy.currency,
            vacancy.city,
            vacancy.street,
            vacancy.building,
            vacancy.fullAddress,
            vacancy.experience,
            vacancy.schedule,
            vacancy.employment,
            vacancy.contactsName,
            vacancy.contactsEmail,
            phoneListToJson(vacancy.contactsPhone),
            vacancy.employerName,
            vacancy.employerLogo,
            vacancy.area,
            vacancy.skills,
            vacancy.url,
            vacancy.industry
        )
    }

    private fun phoneListToJson(phones: List<Phone>): String {
        val gson = Gson()
        val vacancyJson: String = gson.toJson(phones)
        return vacancyJson
    }

    private fun phoneListToJson(phones: String?): List<Phone> {
        val result = mutableListOf<Phone>()
        if (!phones.isNullOrEmpty()) {
            val gson = Gson()
            val listType = object : TypeToken<List<Phone>>() {}.type
            val list: List<Phone> = gson.fromJson(phones, listType)
            result.addAll(list)
        }
        return result
    }
}
