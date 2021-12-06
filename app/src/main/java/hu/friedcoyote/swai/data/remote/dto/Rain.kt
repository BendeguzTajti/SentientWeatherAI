package hu.friedcoyote.swai.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double
)