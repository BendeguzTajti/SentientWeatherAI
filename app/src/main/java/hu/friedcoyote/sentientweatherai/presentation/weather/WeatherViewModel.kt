package hu.friedcoyote.sentientweatherai.presentation.weather

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.friedcoyote.sentientweatherai.common.Resource
import hu.friedcoyote.sentientweatherai.domain.model.DayType
import hu.friedcoyote.sentientweatherai.domain.use_case.GetCurrentWeatherUseCase
import hu.friedcoyote.sentientweatherai.domain.use_case.GetLocationByCityNameUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getLocationByCityNameUseCase: GetLocationByCityNameUseCase
) : ViewModel() {

    private val _dayType = mutableStateOf(DayType.UNKNOWN)
    val dayType: State<DayType> = _dayType

    private val _weatherState = mutableStateOf(WeatherState())
    val weatherState: State<WeatherState> = _weatherState

    private val _searchError = MutableSharedFlow<Throwable?>()
    val searchError: SharedFlow<Throwable?> = _searchError

    init {
        getWeather()
    }

    private fun getWeather() {
        getCurrentWeatherUseCase(47.4979, 19.0402, listOf("minutely")).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _weatherState.value = WeatherState(isLoading = true)
                }
                is Resource.Success -> {
                    result.data?.currentWeather?.let {
                        _dayType.value = it.dayType
                    }
                    _weatherState.value = WeatherState(weather = result.data)
                }
                is Resource.Error -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    @FlowPreview
    fun getWeatherByCityName(cityName: String) {
        getLocationByCityNameUseCase(cityName)
            .catch { _searchError.emit(it) }
            .flatMapMerge { location ->
                getCurrentWeatherUseCase(
                    location.first,
                    location.second,
                    listOf("minutely")
                )
            }
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _searchError.emit(null)
                        _weatherState.value = WeatherState(isLoading = true)
                    }
                    is Resource.Success -> {
                        result.data?.currentWeather?.let {
                            _dayType.value = it.dayType
                        }
                        _weatherState.value = WeatherState(weather = result.data)
                    }
                    is Resource.Error -> {

                    }
                }
            }.launchIn(viewModelScope)
    }
}