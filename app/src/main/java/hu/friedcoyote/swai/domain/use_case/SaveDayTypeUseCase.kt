package hu.friedcoyote.swai.domain.use_case

import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.repository.WeatherRepository
import javax.inject.Inject

class SaveDayTypeUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(dayType: DayType) = repository.saveDayType(dayType)
}