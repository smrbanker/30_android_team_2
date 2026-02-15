package ru.practicum.android.diploma.ui.vacancy.fragment

import ru.practicum.android.diploma.domain.models.Phone

sealed interface VacancyCastItem {
    data class GeneralHeaderItem(
        val vacancyTitle: String,
        val vacancySalary: String
    ) : VacancyCastItem

    data class CompanyItem(
        val employer: String,
        val logo: String,
        val area: String
    ) : VacancyCastItem

    data class ExperienceItem(
        val title: String,
        val experience: String,
        val schedule: String,
        val employment: String
    ) : VacancyCastItem

    data class Item(
        val data: String
    ) : VacancyCastItem

    data class BigHeaderItem(
        val title: String
    ) : VacancyCastItem

    data class SmallHeaderItem(
        val title: String
    ) : VacancyCastItem

    data class SkillItem(
        val skills: String
    ) : VacancyCastItem

    data class MailItem(
        val mail: String
    ) : VacancyCastItem

    data class PhoneItem(
        val phone: Phone
    ) : VacancyCastItem
}
