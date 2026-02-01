package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.models.VacancyDetail

class VacancyResource(val found: Int,
                      val pages: Int,
                      val page: Int,
                      val vacancies: List<Vacancy>) {
}
