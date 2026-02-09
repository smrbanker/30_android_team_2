package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.IndustryState
import java.io.IOException

interface IndustryInteractor {
    suspend fun getIndustries(): IndustryState
}
