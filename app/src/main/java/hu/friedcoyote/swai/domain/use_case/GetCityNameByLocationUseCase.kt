package hu.friedcoyote.swai.domain.use_case

import hu.friedcoyote.swai.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetCityNameByLocationUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(lat: Double, lon: Double): Flow<String> = flow {
        try {
//            val address = repository.getCityNameByLocation(lat, lon)
//            emit(address.locality)
        } catch (e: IOException) {

        }
    }
}