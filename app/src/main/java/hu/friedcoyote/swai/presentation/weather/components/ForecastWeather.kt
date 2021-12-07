package hu.friedcoyote.swai.presentation.weather.components

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import hu.friedcoyote.swai.presentation.weather.WeatherState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ForecastWeather(modifier: Modifier, weatherState: WeatherState) {
    Column(
        modifier = modifier,
    ) {
        var tabRowState by remember { mutableStateOf(0) }
        val titles = listOf("2 órás", "5 napos")
        val context = LocalContext.current
        val hourFormatter = remember {
            val pattern = if (DateFormat.is24HourFormat(context)) "HH:mm" else "hh:mm"
            SimpleDateFormat(pattern, Locale.getDefault())
        }
        val dayFormatter = remember {
            SimpleDateFormat("EEE", Locale.getDefault())
        }
        if (weatherState.weather != null) {
            SideEffect {
                hourFormatter.timeZone = TimeZone.getTimeZone(weatherState.weather.zoneId)
                dayFormatter.timeZone = TimeZone.getTimeZone(weatherState.weather.zoneId)
            }
        }
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = tabRowState,
            backgroundColor = MaterialTheme.colors.surface,
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabRowState == index,
                    onClick = { tabRowState = index }
                )
            }
        }
//        LazyRow(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colors.surface),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//            contentPadding = PaddingValues(18.dp)
//        ) {
//            if (tabRowState == 0) {
//                items(weatherState.weather?.hourlyForecasts ?: emptyList()) { forecast ->
//                    ForecastListItem(
//                        dateFormatter = hourFormatter,
//                        forecast = forecast
//                    )
//                }
//            } else {
//                items(weatherState.weather?.dailyForecasts ?: emptyList()) { forecast ->
//                    ForecastListItem(
//                        dateFormatter = dayFormatter,
//                        forecast = forecast
//                    )
//                }
//            }
//        }
    }
}