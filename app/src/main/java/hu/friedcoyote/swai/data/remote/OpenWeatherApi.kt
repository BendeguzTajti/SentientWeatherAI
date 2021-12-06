package hu.friedcoyote.swai.data.remote

import hu.friedcoyote.swai.data.remote.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("data/2.5/onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("APPID") APPID: String,
        @Query("lang") lang: String
    ): WeatherDto
}