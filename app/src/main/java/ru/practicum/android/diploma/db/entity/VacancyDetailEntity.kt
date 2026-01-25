package ru.practicum.android.diploma.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_vacancies_table")
data class VacancyDetailEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,//Идентификатор вакансии
    val name: String,//Название
    val description: String,//Описание
    val salary: String?,//Зарплата
    val address: String?,//Адрес компании
    val experience: String?,//Требуемый опыт работы
    val schedule: String?,//График
    val employment: String?,//Занятость
    val contacts: String?,//Контакты для связи
    val employer: String,//Наниматель
    val area: String,//Район
    val skills: String,//Навыки
    val url: String,//Ссылка на логотип
    val industry: String,//Отрасль
    @ColumnInfo(name = "insert_time")
    val insertTimestampMillis: Long = System.currentTimeMillis()//Время добавления вакансии в миллисекундах
)
