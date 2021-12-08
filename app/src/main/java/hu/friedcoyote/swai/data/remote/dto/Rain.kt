package hu.friedcoyote.swai.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Rain(
    @SerializedName("1h")
    val h: Double
)