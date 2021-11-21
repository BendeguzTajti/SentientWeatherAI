package hu.friedcoyote.sentientweatherai.presentation.weather

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.friedcoyote.sentientweatherai.domain.model.Day
import hu.friedcoyote.sentientweatherai.presentation.weather.components.Landscape

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherState = viewModel.weatherState.value
    val currentDay = remember { mutableStateOf(Day.MORNING) }
    val transition = updateTransition(currentDay.value, label = "")
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        Box(modifier = Modifier.fillMaxSize().weight(6f), contentAlignment = Alignment.BottomCenter) {
            Landscape(dayChangeTransition = transition)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (weatherState.weather != null) {
                    Text(
                        modifier = Modifier.padding(top = 62.dp, start = 24.dp),
                        text = "${weatherState.weather.temperatureCelsius}°",
                        color = Color.White,
                        style = MaterialTheme.typography.h1,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
                .background(MaterialTheme.colors.surface),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(modifier = Modifier.padding(8.dp),
                onClick = { currentDay.value = Day.MORNING }) {
                Text(text = "REGGEL")
            }
            Button(modifier = Modifier.padding(8.dp), onClick = { currentDay.value = Day.AFTERNOON }) {
                Text(text = "DÉLUTÁN")
            }
            Button(modifier = Modifier.padding(8.dp), onClick = { currentDay.value = Day.NIGHT }) {
                Text(text = "ESTE")
            }
        }
    }
}