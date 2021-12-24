package hu.friedcoyote.swai.domain.use_case

import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCachedDayTypeUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): DayType = repository.getCachedDayType()
}