package hu.friedcoyote.sentientweatherai.domain.model

import java.util.*

data class Forecast(
    val date: Date,
    val temperatureCelsius: Int,
    val temperatureFahrenheit: Int,
    val description: String
)
