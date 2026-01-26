package ru.practicum.android.diploma.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_vacancies_table")
data class VacancyDetailEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val description: String,
    @ColumnInfo(name = "salary_from")
    val salaryFrom: Int?,
    @ColumnInfo(name = "salary_to")
    val salaryTo: Int?,
    val currency: String?,
    val city: String?,
    val street: String?,
    val building: String?,
    @ColumnInfo(name = "full_address")
    val fullAddress: String?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    @ColumnInfo(name = "contacts_name")
    val contactsName: String?,
    @ColumnInfo(name = "contacts_email")
    val contactsEmail: String?,
    @ColumnInfo(name = "contacts_phone")
    val contactsPhone: String?, // List<String>
    @ColumnInfo(name = "employer_name")
    val employerName: String,
    @ColumnInfo(name = "employer_logo")
    val employerLogo: String,
    val area: String,
    val skills: String, // List<String>
    val url: String,
    val industry: String
)
