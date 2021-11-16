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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
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
    val groundColor = transition.animateColor(label = "", transitionSpec = { tween(400) }) {
        when(it) {
            MORNING -> Color(0xFF5A3C6E)
            AFTERNOON -> Color(0xFF3C6157)
            NIGHT -> Color(0xFF1B2F5C)
        }
    }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        Box {
            SunAndMoon(dayChangeTransition = transition)
            LandScape(dayChangeTransition = transition)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(groundColor.value),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(modifier = Modifier.padding(8.dp),
                onClick = { currentDay.value = MORNING }) {
                Text(text = "REGGEL")
            }
            Button(modifier = Modifier.padding(8.dp),onClick = { currentDay.value = AFTERNOON }) {
                Text(text = "DÉLUTÁN")
            }
            Button(modifier = Modifier.padding(8.dp),onClick = { currentDay.value = NIGHT }) {
                Text(text = "ESTE")
            }
        }
    }
}

@Composable
fun SunAndMoon(dayChangeTransition: Transition<Day>) {
    val offset = dayChangeTransition.animateDp(label = "") {
        when(it) {
            MORNING -> 0.dp
            AFTERNOON -> 50.dp
            NIGHT -> 100.dp
        }
    }
    Icon(
        modifier = Modifier
            .height(48.dp)
            .width(48.dp)
            .absoluteOffset(x = offset.value),
        imageVector = Icons.Default.CheckCircle,
        contentDescription = null
    )
}

@Composable
fun LandScape(dayChangeTransition: Transition<Day>) {
    val afternoonLandscapeAlpha = dayChangeTransition.animateFloat(
        label = "",
        transitionSpec = { tween(400) }) {
        if (it == AFTERNOON || it == NIGHT) 1f else 0f
    }
    val nightLandscapeAlpha = dayChangeTransition.animateFloat(
        label = "",
        transitionSpec = { tween(400) }) {
        if (it == NIGHT) 1f else 0f
    }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SentientWeatherAITheme {
        WeatherScreen()
    }
}