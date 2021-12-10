package hu.friedcoyote.swai.domain.model

data class WeatherContainer(
    val zoneId: String,
    val cityName: String,
    val currentWeather: Weather,
    val hourlyForecasts: List<Weather>,
    val dailyForecasts: List<Weather>
)