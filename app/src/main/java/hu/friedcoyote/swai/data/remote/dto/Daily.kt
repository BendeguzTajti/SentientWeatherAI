package hu.friedcoyote.swai.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import hu.friedcoyote.swai.domain.model.Weather
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.roundToInt

@Keep
data class Daily(
    val clouds: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val dt: Long,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike,
    val humidity: Int,
    @SerializedName("moon_phase")
    val moonPhase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val snow: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val uvi: Double,
    val weather: List<WeatherInfo>,
    @SerializedName("wind_deg")
    val windDeg: Int,
    @SerializedName("wind_gust")
    val windGust: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double
)

fun Daily.toForecast(zoneId: ZoneId): Weather {
    val instant = Instant.ofEpochSecond(dt)
    return Weather(
        date = LocalDateTime.ofInstant(instant, zoneId),
        temperatureCelsius = (temp.day - 273.15).roundToInt(),
        temperatureFahrenheit = (((temp.day - 273.15) * 9 / 5) + 32).roundToInt(),
        weatherType = weather.first().main,
        description = weather.first().description,
        windSpeed = windSpeed,
        cloudsPercent = clouds,
        humidityPercent = humidity,
        rainPop = rain,
        snowPop = snow
    )
}