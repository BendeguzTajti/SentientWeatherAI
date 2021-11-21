package hu.friedcoyote.sentientweatherai.presentation.weather

import hu.friedcoyote.sentientweatherai.data.remote.dto.WeatherDto

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: WeatherDto? = null,
    val error: String = ""
)