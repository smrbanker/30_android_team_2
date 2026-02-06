package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.models.Address
import ru.practicum.android.diploma.data.dto.models.AreasDto
import ru.practicum.android.diploma.data.dto.models.Contacts
import ru.practicum.android.diploma.data.dto.models.Employer
import ru.practicum.android.diploma.data.dto.models.Employment
import ru.practicum.android.diploma.data.dto.models.Experience
import ru.practicum.android.diploma.data.dto.models.IndustryDto
import ru.practicum.android.diploma.data.dto.models.Salary
import ru.practicum.android.diploma.data.dto.models.Schedule

data class VacancyDetailResponse(
    val id: String,
    val name: String,
    val description: String,
    val salary: Salary?,
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
) : Response()
