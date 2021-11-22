package hu.friedcoyote.sentientweatherai.presentation.weather.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.friedcoyote.sentientweatherai.R
import hu.friedcoyote.sentientweatherai.data.remote.dto.WeatherType
import hu.friedcoyote.sentientweatherai.domain.model.Forecast

@Composable
fun ForecastListItem(
    forecast: Forecast
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.padding(bottom = 4.dp), text = forecast.timeLabel)
        Icon(
            painter = painterResource(
                id = when (forecast.weatherType) {
                    WeatherType.Clear -> R.drawable.ic_clear_sky_day
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
            contentDescription = forecast.description
        )
        Text(modifier = Modifier.padding(top = 4.dp), text = "${forecast.temperatureCelsius}°")
    }
}