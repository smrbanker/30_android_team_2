package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.db.entity.VacancyDetailEntity
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
            vacancy.phone,
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
            vacancy.contactsPhone,
            vacancy.employerName,
            vacancy.employerLogo,
            vacancy.area,
            vacancy.skills,
            vacancy.url,
            vacancy.industry
        )
    }
}
