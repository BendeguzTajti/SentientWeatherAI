package hu.friedcoyote.swai.data.remote.dto

import androidx.annotation.Keep

@Keep
data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)