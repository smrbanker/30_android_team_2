package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.responses.FilterArea
import ru.practicum.android.diploma.data.dto.responses.FilterIndustry
import ru.practicum.android.diploma.data.dto.responses.VacancyDetail
import ru.practicum.android.diploma.data.dto.responses.VacancyResponse

interface JobApiService {

    companion object {
        private const val TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwcmFjdGljdW0ucnUiLCJhdWQiOiJwcmFjdGljdW0ucnUiLCJ1c2VybmFtZSI6ImFzZCJ9.Nja3aFebErauM_zuBfpE9EMR3oeZmryzgX9Ce-7rsMw"
    }

    @GET("vacancies")
    suspend fun searchVacancies(@Header("Authorization: $TOKEN") @QueryMap options: Map<String, String>): VacancyResponse

    @GET("vacancies/{v_id}")
    suspend fun getVacancy(@Path("v_id") id: Int): VacancyDetail

    @GET("areas")
    suspend fun getCountries(): FilterArea

    @GET("areas/{a_id}")
    suspend fun getRegions(@Path("a_id") areaId: Int): FilterArea

    @GET("industries")
    suspend fun getIndustries(): FilterIndustry
}
