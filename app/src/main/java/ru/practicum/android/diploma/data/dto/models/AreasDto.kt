package ru.practicum.android.diploma.data.dto.models

data class AreasDto( // ВЕРОЯТНЕЕ ВСЕГО ЭТОТ КЛАСС НЕ ПРИГОДИТСЯ, РЕШИМ ПО ХОДУ РАЗРАБОТКИ. ПОКА ПУСТЬ БУДЕТ
    val id: Int,
    val name: String,
    val parentId: Int,
    val areas: List<AreasDto>
)
