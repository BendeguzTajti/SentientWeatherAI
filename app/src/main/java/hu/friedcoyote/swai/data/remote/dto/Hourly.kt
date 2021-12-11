package hu.friedcoyote.swai.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.model.Weather
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.roundToInt

@Keep
data class Hourly(
    val clouds: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val dt: Long,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Rain,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<WeatherInfo>,
    @SerializedName("wind_deg")
    val windDeg: Int,
    @SerializedName("wind_gust")
    val windGust: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double
)

fun Hourly.toForecast(zoneId: ZoneId, currentDate: LocalDate, sunrise: Long, sunset: Long): Weather {
    val instant = Instant.ofEpochSecond(dt)
    val forecastDate = LocalDateTime.ofInstant(instant, zoneId)
    val dayType = if (currentDate == forecastDate.toLocalDate()) {
        if (dt !in (sunrise + 1) until sunset) DayType.NIGHT else DayType.DAY
    } else {
        if (!(dt > (sunrise + 86400) && dt < (sunset + 86400))) DayType.NIGHT else DayType.DAY
    }
    return Weather(
        date = forecastDate,
        dayType = dayType,
        temperatureCelsius = (temp - 273.15).roundToInt(),
        temperatureFahrenheit = (((temp - 273.15) * 9 / 5) + 32).roundToInt(),
        weatherType = weather.first().main,
        description = weather.first().description
    )
}