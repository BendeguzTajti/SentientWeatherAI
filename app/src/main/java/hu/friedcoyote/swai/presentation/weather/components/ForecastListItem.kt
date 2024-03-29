package hu.friedcoyote.swai.presentation.weather.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.friedcoyote.swai.data.remote.dto.WeatherType
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.model.Weather
import hu.friedcoyote.swai.R
import java.time.format.DateTimeFormatter

@Composable
fun ForecastListItem(
    orientation: Int,
    dateFormatter: DateTimeFormatter,
    forecast: Weather
) {
    Column(
        modifier = Modifier.width(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dateFormatter.format(forecast.date),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Icon(
            modifier = Modifier.size(
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) 24.dp else 32.dp
            ),
            painter = painterResource(
                id = when (forecast.weatherType) {
                    WeatherType.Clear -> if (forecast.dayType == DayType.NIGHT) R.drawable.ic_clear_night else R.drawable.ic_clear_day
                    WeatherType.Clouds -> R.drawable.ic_cloud
                    WeatherType.Rain -> R.drawable.ic_rain
                    WeatherType.Thunderstorm -> R.drawable.ic_thunder
                    WeatherType.Tornado -> R.drawable.ic_tornado
                    WeatherType.Fog -> R.drawable.ic_fog
                    WeatherType.Dust -> R.drawable.ic_dust
                    WeatherType.Snow -> R.drawable.ic_snow
                    WeatherType.Smoke -> R.drawable.ic_smoke
                    WeatherType.Drizzle -> R.drawable.ic_drizzle
                    WeatherType.Squall -> R.drawable.ic_squall
                    WeatherType.Mist,
                    WeatherType.Ash,
                    WeatherType.Sand,
                    WeatherType.Haze -> R.drawable.ic_haze
                }
            ),
            contentDescription = forecast.description,
            tint = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "${forecast.temperatureCelsius}°ᶜ",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}