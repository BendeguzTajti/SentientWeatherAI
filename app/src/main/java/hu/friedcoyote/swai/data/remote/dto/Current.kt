package hu.friedcoyote.swai.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.model.Weather
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.roundToInt

@Keep
data class Current(
    val clouds: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val dt: Long,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<WeatherInfo>,
    @SerializedName("wind_deg")
    val windDeg: Int,
    @SerializedName("wind_gust")
    val windGust: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    val rain: Rain,
    val snow: Snow,
)

fun Current.toWeatherData(zoneId: ZoneId, rainPop: Double, snowPop: Double): Weather {
    val instant = Instant.ofEpochSecond(dt)
    val dayType = if (dt !in (sunrise + 1) until sunset) DayType.NIGHT else DayType.DAY
    val windSpeedKmH = (windSpeed.times(3.6).times(100)).roundToInt().div(100.0)
    return Weather(
        date = LocalDateTime.ofInstant(instant, zoneId),
        dayType = dayType,
        temperatureCelsius = (temp - 273.15).roundToInt(),
        temperatureFahrenheit = (((temp - 273.15) * 9 / 5) + 32).roundToInt(),
        weatherType = weather.first().main,
        description = weather.first().description,
        windSpeed = windSpeedKmH,
        cloudsPercent = clouds,
        humidityPercent = humidity,
        rainPop = rainPop,
        snowPop = snowPop
    )
}