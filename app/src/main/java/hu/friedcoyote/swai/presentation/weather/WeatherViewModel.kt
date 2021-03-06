package hu.friedcoyote.swai.presentation.weather

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.friedcoyote.swai.common.Resource
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.use_case.WeatherUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCases: WeatherUseCases
) : ViewModel() {

    private val _dayType = mutableStateOf(
        weatherUseCases.getCachedDayTypeUseCase()
    )
    val dayType: State<DayType> = _dayType

    private val _weatherState = mutableStateOf(WeatherState())
    val weatherState: State<WeatherState> = _weatherState

    private val _searchError = MutableSharedFlow<Int?>()
    val searchError: SharedFlow<Int?> = _searchError

    private var getWeatherJob: Job? = null

    fun onLocationPermissionGranted() {
        if (getWeatherJob == null) {
            getWeatherByUserLocation()
        }
    }

    fun getWeatherByUserLocation(delayInMillis: Long = 500) {
        getWeatherJob?.cancel()
        getWeatherJob = weatherUseCases.getWeatherByLocationUseCase()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _weatherState.value = WeatherState(isLoading = true)
                        delay(delayInMillis)
                    }
                    is Resource.Success -> {
                        result.data?.currentWeather?.let {
                            _dayType.value = it.dayType
                            weatherUseCases.saveDayTypeUseCase(it.dayType)
                        }
                        _weatherState.value = WeatherState(
                            cityName = result.data?.cityName ?: "",
                            currentWeather = result.data?.currentWeather,
                            hourlyForecast = result.data?.hourlyForecasts ?: emptyList(),
                            dailyForecast = result.data?.dailyForecasts ?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        _weatherState.value = WeatherState(
                            initErrorResId = result.errorMessageResId
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }


    fun getWeather(cityName: String, delayInMillis: Long) {
        getWeatherJob?.cancel()
        getWeatherJob = weatherUseCases.getWeatherByCityNameUseCase(cityName)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _searchError.emit(null)
                        _weatherState.value = weatherState.value.copy(
                            isLoading = true,
                        )
                        delay(delayInMillis)
                    }
                    is Resource.Success -> {
                        result.data?.currentWeather?.let {
                            _dayType.value = it.dayType
                        }
                        _weatherState.value = weatherState.value.copy(
                            isLoading = false,
                            cityName = result.data?.cityName ?: "",
                            currentWeather = result.data?.currentWeather,
                            hourlyForecast = result.data?.hourlyForecasts ?: emptyList(),
                            dailyForecast = result.data?.dailyForecasts ?: emptyList(),
                            initErrorResId = null
                        )
                    }
                    is Resource.Error -> {
                        _weatherState.value = weatherState.value.copy(
                            isLoading = false,
                        )
                        if (_weatherState.value.initErrorResId == null) {
                            _searchError.emit(result.errorMessageResId)
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}