package hu.friedcoyote.swai.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import hu.friedcoyote.swai.data.remote.dto.WeatherType
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.model.Weather
import hu.friedcoyote.swai.presentation.ui.theme.SWAITheme
import hu.friedcoyote.swai.presentation.weather.WeatherScreen
import hu.friedcoyote.swai.presentation.weather.WeatherState
import hu.friedcoyote.swai.presentation.weather.components.CurrentWeather
import hu.friedcoyote.swai.presentation.weather.components.ForecastListItem
import hu.friedcoyote.swai.presentation.weather.components.WeatherAppBar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ExperimentalPermissionsApi
@ExperimentalAnimationGraphicsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SWAITheme {
                ProvideWindowInsets {
                    val statusBarPaddings = rememberInsetsPaddingValues(
                        insets = LocalWindowInsets.current.systemBars
                    )
                    WeatherScreen(
                        statusBarPaddings = statusBarPaddings
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationGraphicsApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val titles = listOf("Hourly", "Daily")
    val mockWeather = Weather(
        date = LocalDateTime.now(),
        dayType = DayType.NIGHT,
        temperatureCelsius = 22,
        temperatureFahrenheit = 90,
        weatherType = WeatherType.Clear,
        description = "Clear sky",
        windSpeed = 2.3,
        cloudsPercent = 86,
        humidityPercent = 90,
        rainPop = 0.25,
        snowPop = 0.0
    )
    val weatherState = WeatherState(
        cityName = "Budapest",
        currentWeather = mockWeather,
        hourlyForecast = listOf(mockWeather, mockWeather, mockWeather, mockWeather, mockWeather)
    )
    SWAITheme {
        Scaffold(
            scaffoldState = rememberScaffoldState(),
            backgroundColor = Color(0xFF6963B8),
            topBar = {
                WeatherAppBar(
                    statusBarPaddings = PaddingValues(),
                    weatherState = weatherState,
                    onSearchClicked = { },
                    onMicClicked = { }
                )
            }
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (currentWeather, tabRow, forecastWeather) = createRefs()
                val guideline = createGuidelineFromBottom(0.23f)
                CurrentWeather(
                    orientation = Configuration.ORIENTATION_PORTRAIT,
                    modifier = Modifier
                        .constrainAs(currentWeather) {
                            top.linkTo(parent.top)
                            bottom.linkTo(tabRow.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                            width = Dimension.fillToConstraints
                        },
                    currentWeather = weatherState.currentWeather,
                    dayType = DayType.NIGHT
                )
                TabRow(
                    modifier = Modifier
                        .constrainAs(tabRow) {
                            bottom.linkTo(forecastWeather.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.wrapContent
                            width = Dimension.fillToConstraints
                        },
                    selectedTabIndex = 0,
                    backgroundColor = MaterialTheme.colors.surface,
                ) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = index == 0,
                            onClick = { }
                        )
                    }
                }
                LazyRow(
                    modifier = Modifier
                        .constrainAs(forecastWeather) {
                            top.linkTo(guideline)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                            width = Dimension.fillToConstraints
                        }
                        .background(MaterialTheme.colors.surface),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(14.dp)
                ) {
                    val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
                    items(weatherState.hourlyForecast) { forecast ->
                        ForecastListItem(
                            orientation = Configuration.ORIENTATION_PORTRAIT,
                            dateFormatter = hourFormatter,
                            forecast = forecast
                        )
                    }
                }
            }
        }
    }
}