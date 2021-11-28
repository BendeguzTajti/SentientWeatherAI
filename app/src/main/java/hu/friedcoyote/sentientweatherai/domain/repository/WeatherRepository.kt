package hu.friedcoyote.sentientweatherai.domain.repository

import android.location.Address
import hu.friedcoyote.sentientweatherai.data.remote.dto.WeatherDto

interface WeatherRepository {

    suspend fun getCurrentWeather(lat: Double, lon: Double, exclude: List<String>?): WeatherDto

    fun getLocationByCityName(cityName: String): List<Address>
}