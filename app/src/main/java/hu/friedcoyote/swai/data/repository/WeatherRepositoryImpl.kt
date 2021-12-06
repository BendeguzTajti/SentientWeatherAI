package hu.friedcoyote.swai.data.repository

import android.location.Address
import android.location.Geocoder
import androidx.compose.ui.text.intl.Locale
import hu.friedcoyote.swai.common.Constants
import hu.friedcoyote.swai.data.remote.OpenWeatherApi
import hu.friedcoyote.swai.data.remote.dto.WeatherDto
import hu.friedcoyote.swai.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenWeatherApi,
    private val geocoder: Geocoder
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
    ): WeatherDto {
        return api.getCurrentWeather(
            lat,
            lon,
            "minutely",
            Constants.APP_ID,
            Locale.current.language
        )
    }

    override fun getLocationByCityName(cityName: String): List<Address> {
        return geocoder.getFromLocationName(cityName, 1)
    }
}