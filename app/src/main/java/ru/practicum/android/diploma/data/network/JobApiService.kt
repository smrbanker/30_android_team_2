package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.models.AreasDto
import ru.practicum.android.diploma.data.dto.responses.FilterArea
import ru.practicum.android.diploma.data.dto.responses.FilterIndustry
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailResponse
import ru.practicum.android.diploma.data.dto.responses.VacancyResponse

interface JobApiService {

    companion object {
        private const val TOKEN = BuildConfig.API_ACCESS_TOKEN
    }

    @Headers("Authorization: Bearer $TOKEN")
    @GET("vacancies")
    suspend fun searchVacancies(@QueryMap options: Map<String, String>): VacancyResponse

    @Headers("Authorization: Bearer $TOKEN")
    @GET("vacancies/{v_id}")
    suspend fun getVacancy(@Path("v_id") id: String): VacancyDetailResponse

    @Headers("Authorization: Bearer $TOKEN")
    @GET("areas")
    suspend fun getAreas(): List<AreasDto>

    @GET("areas/{a_id}")
    suspend fun getRegions(@Path("a_id") areaId: Int): FilterArea

    @GET("industries")
    suspend fun getIndustries(): FilterIndustry
}
