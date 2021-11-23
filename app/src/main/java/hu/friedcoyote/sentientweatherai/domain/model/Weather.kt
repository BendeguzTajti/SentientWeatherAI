package hu.friedcoyote.sentientweatherai.domain.model

data class Weather(
    val zoneId: String,
    val temperatureCelsius: Int?,
    val temperatureFahrenheit: Int?,
    val description: String,
    val hourlyForecasts: List<Forecast>,
    val dailyForecasts: List<Forecast>
)