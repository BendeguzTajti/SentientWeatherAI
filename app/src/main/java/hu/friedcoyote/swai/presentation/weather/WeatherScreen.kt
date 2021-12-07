package hu.friedcoyote.swai.presentation.weather

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
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
    ProvideWindowInsets {
        val statusBarPaddings =
            rememberInsetsPaddingValues(insets = LocalWindowInsets.current.systemBars)
        val scaffoldState = rememberScaffoldState()
        val speechRecognizerLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
                val recognizedWords =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (result.resultCode == Activity.RESULT_OK && !recognizedWords.isNullOrEmpty()) {
                    viewModel.getWeatherByCityName(recognizedWords.first())
                }
            }
        val weatherState = viewModel.weatherState
        val dayType = viewModel.dayType
        val dayChangeTransition = updateTransition(
            targetState = dayType.value,
            label = "dayChangeTransition"
        )
        val backgroundColor = dayChangeTransition.animateColor(
            label = "skyColorAnimation",
            transitionSpec = { tween(durationMillis = 300, easing = LinearEasing) }) { type ->
            if (type == DayType.NIGHT) {
                Color(0xFF6963B8)
            } else {
                Color(0xFF87CEEB)
            }
        }
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
                    modifier = Modifier.padding(top = statusBarPaddings.calculateTopPadding()),
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
                        .background(MaterialTheme.colors.surface)
                        .fillMaxSize(),
                    weatherState = weatherState.value
                )
            }
        }
    }
}