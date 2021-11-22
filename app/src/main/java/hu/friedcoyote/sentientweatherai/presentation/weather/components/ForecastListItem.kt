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
import hu.friedcoyote.sentientweatherai.domain.model.Forecast

@Composable
fun ForecastListItem(
    forecast: Forecast
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.padding(bottom = 4.dp), text = forecast.timeLabel)
        Icon(
            painter = painterResource(id = R.drawable.ic_clear_sky_day),
            contentDescription = forecast.description
        )
        Text(modifier = Modifier.padding(top = 4.dp), text = "${forecast.temperatureCelsius}°")
    }
}