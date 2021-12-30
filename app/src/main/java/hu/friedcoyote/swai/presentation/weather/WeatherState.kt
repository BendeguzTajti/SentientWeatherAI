package hu.friedcoyote.swai.presentation.weather

import hu.friedcoyote.swai.domain.model.Weather

data class WeatherState(
    val isLoading: Boolean = false,
    val cityName: String = "",
    val currentWeather: Weather? = null,
    val hourlyForecast: List<Weather> = emptyList(),
    val dailyForecast: List<Weather> = emptyList(),
    val initErrorResId: Int? = null
)