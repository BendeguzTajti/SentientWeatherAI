package hu.friedcoyote.sentientweatherai.presentation.weather.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.friedcoyote.sentientweatherai.R
import hu.friedcoyote.sentientweatherai.domain.model.DayType
import hu.friedcoyote.sentientweatherai.domain.model.Weather
import java.text.SimpleDateFormat

@Composable
fun ForecastListItem(
    dateFormat: SimpleDateFormat,
    forecast: Weather
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = dateFormat.format(forecast.date),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(
                id = if (forecast.dayType == DayType.NIGHT) R.drawable.ic_clear_night else R.drawable.ic_clear_day
//                when (forecast.weatherType) {
//                    WeatherType.Clear -> if (forecast.isNightTime) R.drawable.ic_clear_night else R.drawable.ic_clear_day
//                    WeatherType.Clouds -> R.drawable.ic_cloud
//                    WeatherType.Rain -> R.drawable.ic_rain
//                    WeatherType.Thunderstorm -> R.drawable.ic_thunder
//                    WeatherType.Tornado -> R.drawable.ic_tornado
//                    WeatherType.Fog -> R.drawable.ic_fog
//                    WeatherType.Dust -> R.drawable.ic_dust
//                    WeatherType.Snow -> R.drawable.ic_snow
//                    WeatherType.Smoke -> R.drawable.ic_smoke
//                    WeatherType.Drizzle -> R.drawable.ic_drizzle
//                    WeatherType.Squall -> R.drawable.ic_squall
//                    WeatherType.Mist,
//                    WeatherType.Ash,
//                    WeatherType.Sand,
//                    WeatherType.Haze -> R.drawable.ic_haze
//                }
            ),
            contentDescription = forecast.description,
            tint = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${forecast.temperatureCelsius}°",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}