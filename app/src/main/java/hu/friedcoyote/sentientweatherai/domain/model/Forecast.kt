package hu.friedcoyote.sentientweatherai.domain.model

import java.util.*

data class Forecast(
    val date: Date,
    val timeLabel: String,
    val temperatureCelsius: Int,
    val temperatureFahrenheit: Int,
    val description: String
)