package hu.friedcoyote.swai.domain.model

import hu.friedcoyote.swai.data.remote.dto.WeatherType
import java.time.LocalDateTime

data class Weather(
    val date: LocalDateTime,
    val dayType: DayType = DayType.DAY,
    val temperatureCelsius: Int,
    val temperatureFahrenheit: Int,
    val weatherType: WeatherType,
    val description: String,
    val windSpeed: Double,
    val cloudsPercent: Int,
    val humidityPercent: Int,
    val rainPop: Double,
    val snowPop: Double
)