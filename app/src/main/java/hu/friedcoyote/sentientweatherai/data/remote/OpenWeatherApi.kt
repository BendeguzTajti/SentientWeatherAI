package hu.friedcoyote.sentientweatherai.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    // 24b031540acbc5b0a68d9c8f4692734d

    @GET("/data/2.5/onecall")
    suspend fun getCurrentAndForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("APPID") APPID: String,
        @Query("lang") lang: String
    )
}