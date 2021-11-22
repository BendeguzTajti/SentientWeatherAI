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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import hu.friedcoyote.sentientweatherai.domain.model.Day
import hu.friedcoyote.sentientweatherai.domain.model.Forecast
import hu.friedcoyote.sentientweatherai.presentation.ui.theme.SentientWeatherAITheme
import hu.friedcoyote.sentientweatherai.presentation.weather.WeatherScreen
import hu.friedcoyote.sentientweatherai.presentation.weather.components.ForecastListItem
import hu.friedcoyote.sentientweatherai.presentation.weather.components.Landscape
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SentientWeatherAITheme {
        val currentDay = remember { mutableStateOf(Day.MORNING) }
        val transition = updateTransition(currentDay.value, label = "")
        val hourlyForecasts = listOf(
            Forecast(Date(), 18, 21, ""),
            Forecast(Date(), 18, 21, ""),
            Forecast(Date(), 18, 21, ""),
            Forecast(Date(), 18, 21, ""),
            Forecast(Date(), 18, 21, "")
        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(6f), contentAlignment = Alignment.BottomCenter
            ) {
                Landscape(dayChangeTransition = transition)
                Row(
                    modifier = Modifier.fillMaxSize(),
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
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .weight(3f)
                    .background(MaterialTheme.colors.surface),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 12.dp),
                    text = "Today",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.SemiBold
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    items(hourlyForecasts) { forecast ->
                        ForecastListItem(forecast = forecast)
                    }
                }
            }
        }
    }
}