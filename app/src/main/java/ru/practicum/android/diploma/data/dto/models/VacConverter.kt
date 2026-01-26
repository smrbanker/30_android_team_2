package ru.practicum.android.diploma.data.dto.models

fun vacancyToFull(vacancyDto: VacancyDto): Vacancy = with(vacancyDto) {
    Vacancy(
        id = id,
        name = name,
        description = description,
        salaryFrom = zeroIfNull(salary?.from),
        salaryTo = zeroIfNull(salary?.to),
        currency = emptyIfNull(salary?.currency),
        city = emptyIfNull(address?.city),
        street = emptyIfNull(address?.street),
        building = emptyIfNull(address?.building),
        fullAddress = emptyIfNull(address?.fullAddress),
        experience = emptyIfNull(experience?.name),
        schedule = emptyIfNull(schedule?.name),
        employment = emptyIfNull(employment?.name),
        contact = emptyIfNull(contacts?.name),
        email = emptyIfNull(contacts?.email),
        phone = phoneToString(contacts?.phone),
        employer = employer.name,
        logo = employer.logo,
        area = area.name,
        skills = skillsToString(skills),
        url = url,
        industry = industry.name
    )
}

private fun emptyIfNull(value: String?) = value ?: ""
private fun zeroIfNull(value: Int?) = value ?: 0

fun phoneToString(phones: List<String>?): String {
    if (phones == null) {
        return ""
    }
    val result = StringBuilder()
    phones.forEach { phone ->
        result.append("- ${phone}\n")
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
