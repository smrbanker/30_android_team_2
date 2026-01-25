package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.responses.CountryResponse
import ru.practicum.android.diploma.data.dto.responses.IndustryResponse
import ru.practicum.android.diploma.data.dto.responses.RegionResponse
import ru.practicum.android.diploma.data.dto.responses.SearchResponse
import ru.practicum.android.diploma.data.dto.responses.VacancyResponse

interface JobApiService {

    @GET("vacancies")
    suspend fun searchVacancies(@QueryMap options: Map<String, String>): SearchResponse

    @GET("vacancies/{v_id}")
    suspend fun getVacancy(@Path("v_id") id: Int): VacancyResponse

    @GET("areas")
    suspend fun getCountries(): CountryResponse

    @GET("areas/{a_id}")
    suspend fun getRegions(@Path("a_id") areaId: Int): RegionResponse

    @GET("industries")
    suspend fun getIndustries(): IndustryResponse
}
