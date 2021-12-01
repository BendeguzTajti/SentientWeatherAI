package hu.friedcoyote.sentientweatherai.domain.model

import hu.friedcoyote.sentientweatherai.data.remote.dto.WeatherType
import java.util.*

data class Weather(
    val date: Date,
    val dayType: DayType = DayType.UNKNOWN,
    val temperatureCelsius: Int,
    val temperatureFahrenheit: Int,
    val weatherType: WeatherType,
    val description: String
)