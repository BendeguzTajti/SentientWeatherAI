package hu.friedcoyote.swai.presentation.weather

import android.app.Activity
import android.speech.RecognizerIntent
import android.text.format.DateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.presentation.weather.components.CurrentWeather
import hu.friedcoyote.swai.presentation.weather.components.ForecastListItem
import hu.friedcoyote.swai.presentation.weather.components.WeatherAppBar
import java.time.format.DateTimeFormatter
import java.util.*

@ExperimentalAnimationGraphicsApi
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            val recognizedWords =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result.resultCode == Activity.RESULT_OK && !recognizedWords.isNullOrEmpty()) {
                viewModel.getWeather(recognizedWords.first())
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
    var tabRowState by remember { mutableStateOf(0) }
    val titles = listOf("2 órás", "5 napos")
    val pattern = if (DateFormat.is24HourFormat(LocalContext.current)) "HH:mm" else "hh:mm"
    val searchError = viewModel.searchError.collectAsState(initial = null)
    if (searchError.value != null) {
        LaunchedEffect(searchError.value) {
            scaffoldState.snackbarHostState.showSnackbar(
                "No results found"
            )
        }
    }
    ProvideWindowInsets {
        val statusBarPaddings =
            rememberInsetsPaddingValues(insets = LocalWindowInsets.current.systemBars)
        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = backgroundColor.value,
            topBar = {
                WeatherAppBar(
                    modifier = Modifier.padding(top = statusBarPaddings.calculateTopPadding()),
                    cityName = weatherState.value.cityName,
                    speechRecognizerLauncher = speechRecognizerLauncher
                )
            }
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (currentWeatherContainer, tabRow, forecastWeather) = createRefs()
                val guideline = createGuidelineFromBottom(0.25f)
                CurrentWeather(
                    modifier = Modifier
                        .constrainAs(currentWeatherContainer) {
                            top.linkTo(parent.top)
                            bottom.linkTo(tabRow.top)
                            height = Dimension.fillToConstraints
                        }
                        .fillMaxSize(),
                    currentWeather = weatherState.value.currentWeather
                )
                TabRow(
                    modifier = Modifier
                        .constrainAs(tabRow) {
                            bottom.linkTo(forecastWeather.top)
                            height = Dimension.wrapContent
                        }
                        .fillMaxWidth(),
                    selectedTabIndex = tabRowState,
                    backgroundColor = MaterialTheme.colors.surface,
                ) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = tabRowState == index,
                            onClick = {
                                tabRowState = index
                            }
                        )
                    }
                }
                LazyRow(
                    modifier = Modifier
                        .constrainAs(forecastWeather) {
                            top.linkTo(guideline)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        }
                        .background(MaterialTheme.colors.surface)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(12.dp)
                ) {
                    if (tabRowState == 0) {
                        val hourFormatter = DateTimeFormatter.ofPattern(pattern)
                        items(weatherState.value.hourlyForecast) { forecast ->
                            ForecastListItem(
                                dateFormatter = hourFormatter,
                                forecast = forecast
                            )
                        }
                    } else {
                        val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
                        items(weatherState.value.dailyForecast) { forecast ->
                            ForecastListItem(
                                dateFormatter = dayFormatter,
                                forecast = forecast
                            )
                        }
                    }
                }
            }
        }
    }
}