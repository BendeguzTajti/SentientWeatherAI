package hu.friedcoyote.swai.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import hu.friedcoyote.swai.domain.model.WeatherContainer
import java.time.*

@Keep
data class WeatherDto(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Long
)

fun WeatherDto.toWeather(cityName: String): WeatherContainer {
    val zoneId = ZoneId.of(timezone)
    val instant = Instant.ofEpochSecond(current.dt)
    val currentDate = LocalDateTime.ofInstant(instant, zoneId).toLocalDate()
    return WeatherContainer(
        cityName = cityName,
        currentWeather = current.toWeatherData(zoneId, daily[0].rain, daily[0].snow),
        hourlyForecasts = hourly.drop(1).filterIndexed { i, _ -> i % 2 == 0 }.take(5)
            .map { it.toForecast(zoneId, currentDate, current.sunrise, current.sunset) },
        dailyForecasts = daily.drop(1).take(5).map { it.toForecast(zoneId) }
    )
}