package hu.friedcoyote.swai.data.repository

import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.compose.ui.text.intl.Locale
import androidx.core.content.edit
import hu.friedcoyote.swai.common.Constants
import hu.friedcoyote.swai.common.Constants.CACHED_DAY_TYPE_KEY
import hu.friedcoyote.swai.common.Resource
import hu.friedcoyote.swai.data.remote.OpenWeatherApi
import hu.friedcoyote.swai.data.remote.dto.toWeather
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.model.WeatherContainer
import hu.friedcoyote.swai.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val api: OpenWeatherApi,
    private val geocoder: Geocoder
) : WeatherRepository {

    override fun getCachedDayType(): DayType {
        val cachedDayType = sharedPreferences.getString(CACHED_DAY_TYPE_KEY, null)
        return cachedDayType?.let { DayType.valueOf(it) } ?: DayType.DAY
    }

    override fun saveDayType(dayType: DayType) {
        sharedPreferences.edit {
            putString(CACHED_DAY_TYPE_KEY, dayType.name)
        }
    }

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
            emit(
                Resource.Success(
                    weatherDto.toWeather(address.locality ?: address.adminArea)
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error("Some Http error."))
        } catch (e: IOException) {
            emit(Resource.Error("The server is currently unavailable"))
        }
    }

    override fun getWeatherByCityName(cityNameInput: String): Flow<Resource<WeatherContainer>> =
        flow {
            emit(Resource.Loading())
            try {
                val address = getAddressByCityName(cityNameInput)
                val cityName = address?.locality ?: address?.adminArea
                if (address != null && cityName != null) {
                    val weatherDto = api.getCurrentWeather(
                        address.latitude,
                        address.longitude,
                        "minutely",
                        Constants.APP_ID,
                        Locale.current.language
                    )
                    emit(Resource.Success(weatherDto.toWeather(cityName)))
                } else emit(Resource.Error("Invalid city name."))
            } catch (e: HttpException) {
                emit(Resource.Error("Some Http error."))
            } catch (e: IOException) {
                emit(Resource.Error("The server is currently unavailable."))
            }
        }

    override fun getAddressByCityName(cityNameInput: String): Address? {
        return geocoder.getFromLocationName(cityNameInput, 1).firstOrNull()
    }

    override fun getAddressByLocation(lat: Double, lon: Double): Address {
        return geocoder.getFromLocation(lat, lon, 1).first()
    }
}