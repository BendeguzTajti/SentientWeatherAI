package hu.friedcoyote.swai.presentation.weather

import hu.friedcoyote.swai.domain.model.WeatherContainer

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: WeatherContainer? = null,
    val error: String = ""
)