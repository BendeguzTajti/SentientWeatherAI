package hu.friedcoyote.sentientweatherai.data.remote.dto

import com.google.gson.annotations.SerializedName
import hu.friedcoyote.sentientweatherai.domain.model.DayType
import hu.friedcoyote.sentientweatherai.domain.model.Weather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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

fun Daily.toForecast(hourFormat: SimpleDateFormat): Weather {
    val date = Date(dt * 1000)
    val dayType = when(hourFormat.format(date).toInt()) {
        in 6..11 -> DayType.MORNING
        in 12..17 -> DayType.AFTERNOON
        else -> DayType.NIGHT
    }
    return Weather(
        date = date,
        dayType = dayType,
        temperatureCelsius = (temp.day - 273.15).roundToInt(),
        temperatureFahrenheit = (((temp.day - 273.15) * 9 / 5) + 32).roundToInt(),
        weatherType = weather.first().main,
        description = weather.first().description
    )
}