package hu.friedcoyote.sentientweatherai.presentation.weather

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.friedcoyote.sentientweatherai.common.Resource
import hu.friedcoyote.sentientweatherai.domain.use_case.get_current_weather.GetCurrentWeatherUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ViewModel() {

    private val _weatherState = mutableStateOf(WeatherState())
    val weatherState: State<WeatherState> = _weatherState

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
                    _weatherState.value = WeatherState(weather = result.data)
                }
                is Resource.Error -> {

                }
            }
        }.launchIn(viewModelScope)
    }
}