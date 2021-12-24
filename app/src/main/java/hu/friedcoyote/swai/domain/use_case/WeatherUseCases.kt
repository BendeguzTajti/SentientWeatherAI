package hu.friedcoyote.swai.domain.use_case

data class WeatherUseCases(
    val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    val getWeatherByCityNameUseCase: GetWeatherByCityNameUseCase,
    val getCachedDayTypeUseCase: GetCachedDayTypeUseCase,
    val saveDayTypeUseCase: SaveDayTypeUseCase
)