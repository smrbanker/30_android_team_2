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
    val salary: String?,
    val address: String?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val contacts: String?,
    val employer: String,
    val area: String,
    val skills: String,
    val url: String,
    val industry: String,
    @ColumnInfo(name = "insert_time")
    val insertTimestampMillis: Long = System.currentTimeMillis()
)
