package hu.friedcoyote.sentientweatherai.presentation.weather

import hu.friedcoyote.sentientweatherai.domain.model.WeatherContainer

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: WeatherContainer? = null,
    val error: String = ""
)