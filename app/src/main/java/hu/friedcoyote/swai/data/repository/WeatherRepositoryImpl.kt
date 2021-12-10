package hu.friedcoyote.swai.data.repository

import android.location.Address
import android.location.Geocoder
import androidx.compose.ui.text.intl.Locale
import hu.friedcoyote.swai.common.Constants
import hu.friedcoyote.swai.common.Resource
import hu.friedcoyote.swai.data.remote.OpenWeatherApi
import hu.friedcoyote.swai.data.remote.dto.toWeather
import hu.friedcoyote.swai.domain.model.WeatherContainer
import hu.friedcoyote.swai.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenWeatherApi,
    private val geocoder: Geocoder
) : WeatherRepository {

    override fun getWeatherByLocation(
        lat: Double,
        lon: Double,
    ): Flow<Resource<WeatherContainer>> = flow {
        emit(Resource.Loading())
        try {
            val address = getAddressByLocation(lat, lon)
            val weatherDto = api.getCurrentWeather(
                lat,
                lon,
                "minutely",
                Constants.APP_ID,
                Locale.current.language
            )
            emit(Resource.Success(weatherDto.toWeather(address.locality)))
        } catch (e: HttpException) {
            emit(Resource.Error("Some Http error."))
        } catch (e: IOException) {
            emit(Resource.Error("The server is currently unavailable"))
        }
    }

    override fun getWeatherByCityName(cityName: String): Flow<Resource<WeatherContainer>> = flow {
        emit(Resource.Loading())
        try {
            val address = getAddressByCityName(cityName)
            address?.let {
                val weatherDto = api.getCurrentWeather(
                    it.latitude,
                    it.longitude,
                    "minutely",
                    Constants.APP_ID,
                    Locale.current.language
                )
                emit(Resource.Success(weatherDto.toWeather(it.locality)))
            } ?: emit(Resource.Error("Invalid city name."))
        } catch (e: HttpException) {
            emit(Resource.Error("Some Http error."))
        } catch (e: IOException) {
            emit(Resource.Error("The server is currently unavailable."))
        }
    }

    override fun getAddressByCityName(cityName: String): Address? {
        return geocoder.getFromLocationName(cityName, 1).firstOrNull()
    }

    override fun getAddressByLocation(lat: Double, lon: Double): Address {
        return geocoder.getFromLocation(lat, lon, 1).first()
    }
}