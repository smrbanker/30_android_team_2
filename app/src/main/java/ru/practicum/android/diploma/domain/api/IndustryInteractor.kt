package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.IndustryState

interface IndustryInteractor {
    suspend fun getIndustries(): IndustryState
}
