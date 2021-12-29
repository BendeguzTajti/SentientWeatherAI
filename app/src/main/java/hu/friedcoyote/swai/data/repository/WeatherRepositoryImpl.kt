package hu.friedcoyote.swai.data.repository

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.compose.ui.text.intl.Locale
import androidx.core.content.edit
import com.google.android.gms.location.FusedLocationProviderClient
import hu.friedcoyote.swai.R
import hu.friedcoyote.swai.common.Constants
import hu.friedcoyote.swai.common.Constants.CACHED_DAY_TYPE_KEY
import hu.friedcoyote.swai.common.Resource
import hu.friedcoyote.swai.data.remote.OpenWeatherApi
import hu.friedcoyote.swai.data.remote.dto.toWeather
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.model.WeatherContainer
import hu.friedcoyote.swai.domain.repository.WeatherRepository
import hu.friedcoyote.swai.util.awaitLastLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WeatherRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val api: OpenWeatherApi,
    private val geocoder: Geocoder,
    private val fusedLocationProviderClient: FusedLocationProviderClient
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

    @SuppressLint("MissingPermission")
    override fun getWeatherByUserLocation(): Flow<Resource<WeatherContainer>> = flow {
        emit(Resource.Loading())
        try {
            val location = fusedLocationProviderClient.awaitLastLocation()
            val address = getAddressByLocation(location.latitude, location.longitude)
            val weatherDto = api.getCurrentWeather(
                location.latitude,
                location.longitude,
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
            emit(Resource.Error(R.string.server_error))
        } catch (e: UnknownHostException) {
            emit(Resource.Error(R.string.internet_error))
        } catch (e: IOException) {
            // This happens when geocoder fails
            // Set the error message accordingly
            emit(Resource.Error(R.string.internet_error))
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
                } else emit(Resource.Error(R.string.no_results_found))
            } catch (e: HttpException) {
                emit(Resource.Error(R.string.server_error))
            } catch (e: UnknownHostException) {
                emit(Resource.Error(R.string.internet_error))
            } catch (e: IOException) {
                // This happens when geocoder fails
                // Set the error message accordingly
                emit(Resource.Error(R.string.internet_error))
            }
        }

    override fun getAddressByCityName(cityNameInput: String): Address? {
        return geocoder.getFromLocationName(cityNameInput, 1).firstOrNull()
    }

    override fun getAddressByLocation(lat: Double, lon: Double): Address {
        return geocoder.getFromLocation(lat, lon, 1).first()
    }
}