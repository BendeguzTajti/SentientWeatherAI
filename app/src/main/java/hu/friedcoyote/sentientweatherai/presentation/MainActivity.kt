package hu.friedcoyote.sentientweatherai.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import hu.friedcoyote.sentientweatherai.R
import hu.friedcoyote.sentientweatherai.data.remote.dto.WeatherType
import hu.friedcoyote.sentientweatherai.domain.model.DayType
import hu.friedcoyote.sentientweatherai.domain.model.Weather
import hu.friedcoyote.sentientweatherai.presentation.ui.theme.SentientWeatherAITheme
import hu.friedcoyote.sentientweatherai.presentation.weather.WeatherScreen
import hu.friedcoyote.sentientweatherai.presentation.weather.components.ForecastListItem
import hu.friedcoyote.sentientweatherai.presentation.weather.components.Landscape
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SentientWeatherAITheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    WeatherScreen()
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
//    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun DefaultPreview() {
    SentientWeatherAITheme {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentDay = remember { mutableStateOf(DayType.MORNING) }
        val transition = updateTransition(currentDay.value, label = "")
        val hourlyForecasts = listOf(
            Weather(Date(), DayType.MORNING, 18, 21, WeatherType.Clear, ""),
            Weather(Date(), DayType.MORNING, 18, 21, WeatherType.Tornado, ""),
            Weather(Date(), DayType.MORNING, 18, 21, WeatherType.Rain, ""),
            Weather(Date(), DayType.MORNING, 18, 21, WeatherType.Thunderstorm, ""),
            Weather(Date(), DayType.MORNING, 18, 21, WeatherType.Haze, "")
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Landscape(dayChangeTransition = transition)
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 28.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(top = 62.dp, start = 24.dp),
                        text = "18°",
                        color = Color.White,
                        style = MaterialTheme.typography.h1,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 18.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    FloatingActionButton(
                        onClick = { /*do something*/ },
                        backgroundColor = MaterialTheme.colors.surface,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mic),
                            contentDescription = "Localized description"
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(start = 18.dp, end = 18.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 12.dp),
                    text = "Today",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    items(hourlyForecasts) { forecast ->
                        ForecastListItem(dateFormat = dateFormat, forecast = forecast)
                    }
                }
            }
        }
    }
}