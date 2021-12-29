package hu.friedcoyote.swai.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
import hu.friedcoyote.swai.presentation.weather.components.CurrentWeather
import java.time.LocalDateTime

@ExperimentalPermissionsApi
@ExperimentalAnimationGraphicsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    SWAITheme {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (currentWeather, tabRow, forecastWeather) = createRefs()
            val guideline = createGuidelineFromBottom(0.2f)
            CurrentWeather(
                orientation = Configuration.ORIENTATION_PORTRAIT,
                modifier = Modifier
                    .background(Color(0xFF6963B8))
                    .constrainAs(currentWeather) {
                        top.linkTo(parent.top)
                        bottom.linkTo(tabRow.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    },
                currentWeather = Weather(
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
                ),
                dayType = DayType.NIGHT
            )
            Box(modifier = Modifier
                .background(Color.Blue)
                .constrainAs(tabRow) {
                    bottom.linkTo(forecastWeather.top)
                    height = Dimension.value(75.dp)
                }
                .fillMaxWidth())
            Box(modifier = Modifier
                .background(Color.Red)
                .constrainAs(forecastWeather) {
                    top.linkTo(guideline)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth())
        }
    }
}