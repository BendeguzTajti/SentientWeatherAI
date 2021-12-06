package hu.friedcoyote.swai.data.remote.dto

data class WeatherInfo(
    val description: String,
    val icon: String,
    val id: Int,
    val main: WeatherType
)