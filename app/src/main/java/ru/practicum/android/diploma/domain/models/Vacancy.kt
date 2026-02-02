package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val name: String,
    val salary: Salary?,
    val employer: Employer?,
    val area: Area?,
    val snippet: Snippet?
)

data class Salary(
    val from: Int?,
    val to: Int?,
    val currency: String?
)

data class Employer(
    val id: String?,
    val name: String?,
    val logoUrl: String?
)

data class Area(
    val id: String?,
    val name: String?
)

data class Snippet(
    val requirement: String?,
    val responsibility: String?
)

