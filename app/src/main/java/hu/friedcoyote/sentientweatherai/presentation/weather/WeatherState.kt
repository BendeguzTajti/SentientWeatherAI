package hu.friedcoyote.sentientweatherai.presentation.weather

import hu.friedcoyote.sentientweatherai.domain.model.Weather

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String = ""
)