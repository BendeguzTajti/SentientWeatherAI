package hu.friedcoyote.sentientweatherai.data.remote.dto

import com.google.gson.annotations.SerializedName
import hu.friedcoyote.sentientweatherai.domain.model.WeatherContainer
import java.text.SimpleDateFormat
import java.util.*

data class WeatherDto(
    val current: Current,
    val daily: List<Daily>?,
    val hourly: List<Hourly>?,
    val lat: Double,
    val lon: Double,
    val minutely: List<Minutely>?,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
)

fun WeatherDto.toWeather(): WeatherContainer {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        this.timeZone = TimeZone.getTimeZone(timezone)
    }
    val currentDate = dateFormat.format(current.dt * 1000)
    return WeatherContainer(
        zoneId = timezone,
        currentWeather = current.toWeatherData(),
        hourlyForecasts = hourly?.drop(1)?.filterIndexed { i, _ -> i % 2 == 0 }?.take(5)
            ?.map { it.toForecast(dateFormat, currentDate, current.sunrise, current.sunset) } ?: emptyList(),
        dailyForecasts = daily?.drop(1)?.take(5)?.map { it.toForecast() } ?: emptyList()
    )
}