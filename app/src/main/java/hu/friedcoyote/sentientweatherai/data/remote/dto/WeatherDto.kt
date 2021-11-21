package hu.friedcoyote.sentientweatherai.data.remote.dto

import com.google.gson.annotations.SerializedName
import hu.friedcoyote.sentientweatherai.domain.model.Weather
import kotlin.math.roundToInt

data class WeatherDto(
    val current: Current?,
    val daily: List<Daily>?,
    val hourly: List<Hourly>?,
    val lat: Double,
    val lon: Double,
    val minutely: List<Minutely>?,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
)

fun WeatherDto.toWeather(): Weather {
    return Weather(
        temperatureCelsius = if (current != null) (current.temp - 273.15).roundToInt() else null,
        temperatureFahrenheit = if (current != null) (((current.temp - 273.15) * 9 / 5) + 32).roundToInt() else null,
        description = current?.weather?.firstOrNull()?.description ?: "",
        hourlyForecasts = hourly?.filterIndexed { i, _ -> i != 0 && i % 3 == 0 }?.take(5)
            ?.map { it.toForecast() } ?: emptyList(),
        dailyForecasts = daily?.drop(1)?.take(5)?.map { it.toForecast() } ?: emptyList()
    )
}