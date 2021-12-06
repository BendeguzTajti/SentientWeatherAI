package hu.friedcoyote.swai.domain.repository

import android.location.Address
import hu.friedcoyote.swai.data.remote.dto.WeatherDto

interface WeatherRepository {

    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherDto

    fun getLocationByCityName(cityName: String): List<Address>
}