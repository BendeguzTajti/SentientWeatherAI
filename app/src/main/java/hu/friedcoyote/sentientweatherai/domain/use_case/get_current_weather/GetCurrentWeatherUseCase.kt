package hu.friedcoyote.sentientweatherai.domain.use_case.get_current_weather

import hu.friedcoyote.sentientweatherai.common.Resource
import hu.friedcoyote.sentientweatherai.data.remote.dto.toWeather
import hu.friedcoyote.sentientweatherai.domain.model.Weather
import hu.friedcoyote.sentientweatherai.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(exclude: List<String>?): Flow<Resource<Weather>> = flow {
        try {
            emit(Resource.Loading<Weather>())
            val weather = repository.getCurrentWeather(47.4979, 19.0402, exclude)
            emit(Resource.Success<Weather>(weather.toWeather()))
        } catch (e: HttpException) {
            emit(Resource.Error<Weather>("ERROR"))
        } catch (e: IOException) {
            emit(Resource.Error<Weather>("ERROR"))
        }
    }
}