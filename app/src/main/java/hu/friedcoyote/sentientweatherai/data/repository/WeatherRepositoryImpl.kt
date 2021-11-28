package hu.friedcoyote.sentientweatherai.data.repository

import android.location.Address
import android.location.Geocoder
import androidx.compose.ui.text.intl.Locale
import hu.friedcoyote.sentientweatherai.common.Constants
import hu.friedcoyote.sentientweatherai.data.remote.OpenWeatherApi
import hu.friedcoyote.sentientweatherai.data.remote.dto.WeatherDto
import hu.friedcoyote.sentientweatherai.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenWeatherApi,
    private val geocoder: Geocoder
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        exclude: List<String>?
    ): WeatherDto {
        return api.getCurrentWeather(lat, lon, exclude?.joinToString(separator = ","), Constants.APP_ID, Locale.current.language)
    }

    override fun getLocationByCityName(cityName: String): List<Address> {
        return geocoder.getFromLocationName(cityName, 1)
    }
}