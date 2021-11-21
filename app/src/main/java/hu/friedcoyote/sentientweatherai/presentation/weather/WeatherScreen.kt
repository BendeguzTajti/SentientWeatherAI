package hu.friedcoyote.sentientweatherai.presentation.weather

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.friedcoyote.sentientweatherai.R
import hu.friedcoyote.sentientweatherai.domain.model.Day

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val currentDay = remember { mutableStateOf(Day.MORNING) }
    val transition = updateTransition(currentDay.value, label = "")
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        Box(modifier = Modifier.weight(6f), contentAlignment = Alignment.BottomCenter) {
            LandScape(dayChangeTransition = transition)
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

@Composable
fun LandScape(dayChangeTransition: Transition<Day>) {
    val skyColor = dayChangeTransition.animateColor(
        label = "skyColorAnimation",
        transitionSpec = { tween(400) }) {
        when (it) {
            Day.MORNING -> Color(0xFFEFA093)
            Day.AFTERNOON -> Color(0xFF52DCFF)
            Day.NIGHT -> Color(0xFF6963B8)
        }
    }
    val afternoonLandscapeAlpha = dayChangeTransition.animateFloat(
        label = "afternoonLandscapeAlphaAnimation",
        transitionSpec = { tween(400) }) {
        if (it == Day.AFTERNOON) 1f else 0f
    }
    val nightLandscapeAlpha = dayChangeTransition.animateFloat(
        label = "nightLandscapeAlphaAnimation",
        transitionSpec = { tween(400) }) {
        if (it == Day.NIGHT) 1f else 0f
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = skyColor.value),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.landscape_morning),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.landscape_afternoon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = afternoonLandscapeAlpha.value
        )
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.landscape_night),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = nightLandscapeAlpha.value
        )
    }
}