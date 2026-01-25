package ru.practicum.android.diploma.data.dto.models

data class VacancyDto(
    val id: String,
    val name: String,
    val description: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String,
    val address: Address?,
    val experience: Experience?,
    val schedule: Schedule?,
    val employment: Employment?,
    val contacts: Contacts?,
    val employer: Employer,
    val area: AreasDto,
    val skills: List<String>,
    val url: String,
    val industry: IndustryDto
)

data class Address(
    val city: String,
    val street: String,
    val building: String,
    val fullAddress: String
)

data class Experience(
    val id: String,
    val name: String
)

data class Schedule(
    val id: String,
    val name: String
)

data class Employment(
    val id: String,
    val name: String
)

data class Contacts(
    val id: String,
    val name: String,
    val email: String,
    val phone: List<String>
)

data class Employer(
    val id: String,
    val name: String,
    val logo: String
)
