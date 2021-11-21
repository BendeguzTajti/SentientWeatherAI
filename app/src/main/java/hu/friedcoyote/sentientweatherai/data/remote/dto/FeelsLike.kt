package hu.friedcoyote.sentientweatherai.data.remote.dto

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)