package hu.friedcoyote.swai.presentation.weather

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.presentation.weather.components.CurrentWeather
import hu.friedcoyote.swai.presentation.weather.components.ForecastWeather
import hu.friedcoyote.swai.presentation.weather.components.WeatherAppBar
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalAnimationGraphicsApi
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            val recognizedWords =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result.resultCode == Activity.RESULT_OK && !recognizedWords.isNullOrEmpty()) {
                viewModel.getWeatherByCityName(recognizedWords.first())
            }
        }
    val scaffoldState = rememberScaffoldState()
    val weatherState = viewModel.weatherState
    val dayType = viewModel.dayType
    val dayChangeTransition = updateTransition(
        targetState = dayType.value,
        label = "dayChangeTransition"
    )
    val backgroundColor = dayChangeTransition.animateColor(
        label = "skyColorAnimation",
        transitionSpec = { tween(400) }) { type ->
        if (type == DayType.NIGHT) {
            Color(0xFF6963B8)
        } else {
            Color(0xFF87CEEB)
        }
    }
    systemUiController.setSystemBarsColor(
        color = backgroundColor.value,
        darkIcons = false,
        isNavigationBarContrastEnforced = false
    )
    val searchError = viewModel.searchError.collectAsState(initial = null)
    if (searchError.value != null) {
        LaunchedEffect(searchError.value) {
            scaffoldState.snackbarHostState.showSnackbar(
                "No results found"
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = backgroundColor.value,
        topBar = {
            WeatherAppBar(
                cityName = "Budapest",
                speechRecognizerLauncher = speechRecognizerLauncher
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CurrentWeather(
                modifier = Modifier
                    .weight(.7f)
                    .fillMaxSize(),
                dayType = dayType.value
            )
            ForecastWeather(
                modifier = Modifier
                    .weight(.3f)
                    .fillMaxSize(),
                weatherState = weatherState.value
            )
        }
    }
}