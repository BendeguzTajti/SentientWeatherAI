package hu.friedcoyote.swai.domain.use_case

import hu.friedcoyote.swai.common.Resource
import hu.friedcoyote.swai.data.remote.dto.toWeather
import hu.friedcoyote.swai.domain.model.WeatherContainer
import hu.friedcoyote.swai.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(lat: Double, lon: Double): Flow<Resource<WeatherContainer>> = flow {
        try {
            emit(Resource.Loading<WeatherContainer>())
            val weather = repository.getCurrentWeather(lat, lon)
            emit(Resource.Success<WeatherContainer>(weather.toWeather()))
        } catch (e: HttpException) {
            emit(Resource.Error<WeatherContainer>("ERROR"))
        } catch (e: IOException) {
            emit(Resource.Error<WeatherContainer>("ERROR"))
        }
    }
}