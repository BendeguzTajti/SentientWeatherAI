package hu.friedcoyote.sentientweatherai.data.remote.dto

import com.google.gson.annotations.SerializedName
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

fun Hourly.toForecast(dateFormatter: SimpleDateFormat, currentDate: String, sunrise: Long, sunset: Long): Weather {
    val date = Date(dt * 1000)

    val isNightTime = if (currentDate == dateFormatter.format(date)) {
        dt !in (sunrise + 1) until sunset
    } else {
        !(dt > (sunrise + 86400) && dt < (sunset + 86400))
    }

    return Weather(
        date = date,
        isNightTime = isNightTime,
        temperatureCelsius = (temp - 273.15).roundToInt(),
        temperatureFahrenheit = (((temp - 273.15) * 9 / 5) + 32).roundToInt(),
        weatherType = weather.first().main,
        description = weather.first().description
    )
}