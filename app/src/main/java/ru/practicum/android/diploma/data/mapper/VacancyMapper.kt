package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.SnippetDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Snippet
import ru.practicum.android.diploma.domain.models.Vacancy

object VacancyMapper {
    fun VacancyDto.toDomain(): Vacancy {
        return Vacancy(
            id = id,
            name = name,
            salary = salary?.toDomain(),
            employer = employer?.toDomain(),
            area = area?.toDomain(),
            snippet = snippet?.toDomain()
        )
    }

    private fun SalaryDto.toDomain(): Salary {
        return Salary(
            from = from,
            to = to,
            currency = currency
        )
    }

    private fun EmployerDto.toDomain(): Employer {
        return Employer(
            id = id,
            name = name,
            logoUrl = logoUrls?.original
        )
    }

    private fun AreaDto.toDomain(): Area {
        return Area(
            id = id,
            name = name
        )
    }

    private fun SnippetDto.toDomain(): Snippet {
        return Snippet(
            requirement = requirement,
            responsibility = responsibility
        )
    }
}

