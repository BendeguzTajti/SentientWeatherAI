package hu.friedcoyote.sentientweatherai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.friedcoyote.sentientweatherai.model.Day
import hu.friedcoyote.sentientweatherai.model.Day.*
import hu.friedcoyote.sentientweatherai.ui.theme.SentientWeatherAITheme

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

@Composable
fun WeatherScreen() {
    val currentDay = remember { mutableStateOf(MORNING) }
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
                onClick = { currentDay.value = MORNING }) {
                Text(text = "REGGEL")
            }
            Button(modifier = Modifier.padding(8.dp), onClick = { currentDay.value = AFTERNOON }) {
                Text(text = "DÉLUTÁN")
            }
            Button(modifier = Modifier.padding(8.dp), onClick = { currentDay.value = NIGHT }) {
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
            MORNING -> Color(0xFFEFA093)
            AFTERNOON -> Color(0xFF52DCFF)
            NIGHT -> Color(0xFF6963B8)
        }
    }
    val afternoonLandscapeAlpha = dayChangeTransition.animateFloat(
        label = "afternoonLandscapeAlphaAnimation",
        transitionSpec = { tween(400) }) {
        if (it == AFTERNOON) 1f else 0f
    }
    val nightLandscapeAlpha = dayChangeTransition.animateFloat(
        label = "nightLandscapeAlphaAnimation",
        transitionSpec = { tween(400) }) {
        if (it == NIGHT) 1f else 0f
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SentientWeatherAITheme {
        WeatherScreen()
    }
}