package hu.friedcoyote.sentientweatherai.data.remote.dto

import com.google.gson.annotations.SerializedName
import hu.friedcoyote.sentientweatherai.domain.model.DayType
import hu.friedcoyote.sentientweatherai.domain.model.Weather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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

fun Hourly.toForecast(hourFormat: SimpleDateFormat): Weather {
    val date = Date(dt * 1000)
    val dayType = when(hourFormat.format(date).toInt()) {
        in 6..11 -> DayType.MORNING
        in 12..17 -> DayType.AFTERNOON
        else -> DayType.NIGHT
    }
    return Weather(
        date = date,
        dayType = dayType,
        temperatureCelsius = (temp - 273.15).roundToInt(),
        temperatureFahrenheit = (((temp - 273.15) * 9 / 5) + 32).roundToInt(),
        weatherType = weather.first().main,
        description = weather.first().description
    )
}