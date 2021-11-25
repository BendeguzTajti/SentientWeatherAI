package hu.friedcoyote.sentientweatherai.presentation.weather

import android.text.format.DateFormat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.friedcoyote.sentientweatherai.R
import hu.friedcoyote.sentientweatherai.presentation.weather.components.ForecastListItem
import hu.friedcoyote.sentientweatherai.presentation.weather.components.Landscape
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val pattern = if (DateFormat.is24HourFormat(LocalContext.current)) "HH:mm" else "hh:mm"
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    val weatherState = viewModel.weatherState.value
    val transition = updateTransition(viewModel.dayType.value, label = "")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.7f), contentAlignment = Alignment.BottomCenter
        ) {
            Landscape(dayChangeTransition = transition)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (weatherState.weather != null) {
                    Text(
                        modifier = Modifier.padding(top = 62.dp, start = 24.dp),
                        text = "${weatherState.weather.currentWeather?.temperatureCelsius}°",
                        color = Color.White,
                        style = MaterialTheme.typography.h1,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 18.dp),
                horizontalArrangement = Arrangement.End
            ) {
                FloatingActionButton(
                    onClick = { /*do something*/ },
                    backgroundColor = MaterialTheme.colors.surface
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
            if (weatherState.weather != null) {
                dateFormat.timeZone = TimeZone.getTimeZone(weatherState.weather.zoneId)
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
                    items(weatherState.weather.hourlyForecasts) { forecast ->
                        ForecastListItem(dateFormat = dateFormat, forecast = forecast)
                    }
                }
            }
        }
    }
}