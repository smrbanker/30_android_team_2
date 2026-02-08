package ru.practicum.android.diploma.data.dto.models

import ru.practicum.android.diploma.data.dto.responses.VacancyDetailResponse
import ru.practicum.android.diploma.domain.models.Vacancy

fun vacancyToFull(vacancyDetail: VacancyDetail): Vacancy = with(vacancyDetail) {
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
        phone = phoneToString(contacts?.phones),
        employer = employer.name,
        logo = employer.logo,
        area = area.name,
        skills = skillsToString(skills),
        url = url,
        industry = industry.name
    )
}
fun vacancyToFullFromDetailResponse(vacancyDetail: VacancyDetailResponse): Vacancy = with(vacancyDetail) {
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
        phone = phoneToString(contacts?.phones),
        employer = employer.name,
        logo = employer.logo,
        area = area.name,
        skills = skillsToString(skills),
        url = url,
        industry = industry.name
    )
}

fun phoneToString(phones: List<Phones>?): String {
    val list = mutableListOf<String>()
    if (phones.isNullOrEmpty()) {
        return ""
    }
    val result = StringBuilder()
    phones.forEach { phone ->
        result.append("${phone.formatted}\n")
    }
    return result.toString()
}

fun skillsToString(skills: List<String>): String {
    val result = StringBuilder()
    skills.forEach { skill ->
        result.append("• ${skill}\n")
    }
    return result.toString()
}
