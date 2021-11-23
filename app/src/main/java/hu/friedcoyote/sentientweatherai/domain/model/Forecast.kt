package hu.friedcoyote.sentientweatherai.domain.model

import hu.friedcoyote.sentientweatherai.data.remote.dto.WeatherType
import java.util.*

data class Forecast(
    val date: Date,
    val temperatureCelsius: Int,
    val temperatureFahrenheit: Int,
    val weatherType: WeatherType,
    val description: String
)