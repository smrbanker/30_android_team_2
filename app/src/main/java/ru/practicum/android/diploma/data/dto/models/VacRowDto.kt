package ru.practicum.android.diploma.data.dto.models

data class VacRowDto(
    val id: String,
    val name: String,
    val description: String,
    val salaryFrom: Int,
    val salaryTo: Int,
    val currency: String,
    val city: String,
    val street: String,
    val building: String,
    val fullAddress: String,
    val experience: String,
    val schedule: String,
    val employment: String,
    val contact: String,
    val email: String,
    val phone: String,
    val employer: String,
    val logo: String,
    val area: String,
    val skills: String,
    val url: String,
    val industry: String
)
