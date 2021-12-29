package hu.friedcoyote.swai.domain.use_case

import hu.friedcoyote.swai.common.Resource
import hu.friedcoyote.swai.domain.model.WeatherContainer
import hu.friedcoyote.swai.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherByCityNameUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    operator fun invoke(cityName: String): Flow<Resource<WeatherContainer>> =
        repository.getWeatherByCityName(cityName)
}