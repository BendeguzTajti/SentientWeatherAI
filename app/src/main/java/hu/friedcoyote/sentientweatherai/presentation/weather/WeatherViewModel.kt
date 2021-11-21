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
        getCurrentWeatherUseCase(listOf("minutely")).onEach { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                }
                is Resource.Error -> {

                }
            }
        }.launchIn(viewModelScope)
    }
}