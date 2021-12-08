package hu.friedcoyote.swai.data.remote.dto

import androidx.annotation.Keep

@Keep
data class WeatherInfo(
    val description: String,
    val icon: String,
    val id: Int,
    val main: WeatherType
)