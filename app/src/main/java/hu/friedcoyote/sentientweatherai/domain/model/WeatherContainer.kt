package hu.friedcoyote.sentientweatherai.domain.model

data class WeatherContainer(
    val zoneId: String,
    val currentWeather: Weather,
    val hourlyForecasts: List<Weather>,
    val dailyForecasts: List<Weather>
)