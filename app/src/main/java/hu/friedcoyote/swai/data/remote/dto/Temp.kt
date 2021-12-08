package hu.friedcoyote.swai.data.remote.dto

import androidx.annotation.Keep

@Keep
data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
)