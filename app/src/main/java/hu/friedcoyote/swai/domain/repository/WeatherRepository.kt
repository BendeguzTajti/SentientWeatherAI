package hu.friedcoyote.swai.domain.repository

import android.location.Address
import hu.friedcoyote.swai.common.Resource
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.model.WeatherContainer
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getCachedDayType(): DayType

    fun saveDayType(dayType: DayType)

    fun getWeatherByLocation(lat: Double, lon: Double): Flow<Resource<WeatherContainer>>

    fun getWeatherByCityName(cityNameInput: String): Flow<Resource<WeatherContainer>>

    fun getAddressByCityName(cityNameInput: String): Address?

    fun getAddressByLocation(lat: Double, lon: Double): Address
}