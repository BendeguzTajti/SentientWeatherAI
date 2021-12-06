package hu.friedcoyote.swai.domain.use_case

import hu.friedcoyote.swai.common.InvalidCityNameException
import hu.friedcoyote.swai.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLocationByCityNameUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    @Throws(InvalidCityNameException::class)
    operator fun invoke(cityName: String): Flow<Pair<Double, Double>> = flow {
        try {
            val addresses = repository.getLocationByCityName(cityName)
            if (addresses.isNotEmpty()) {
                val location = addresses.first()
                emit(Pair(location.latitude, location.longitude))
            } else {
                throw InvalidCityNameException("Invalid city name.")
            }
        } catch (e: Exception) {
            throw InvalidCityNameException("Invalid city name.")
        }
    }
}