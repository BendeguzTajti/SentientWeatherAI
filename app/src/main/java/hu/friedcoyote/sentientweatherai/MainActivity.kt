package hu.friedcoyote.sentientweatherai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
        Box {
            LandScape(dayChangeTransition = transition)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { currentDay.value = MORNING }) {
                Text(text = "REGGEL")
            }
            Button(onClick = { currentDay.value = AFTERNOON }) {
                Text(text = "DÉLUTÁN")
            }
            Button(onClick = { currentDay.value = NIGHT }) {
                Text(text = "ESTE")
            }
        }
    }
}

@Composable
fun LandScape(dayChangeTransition: Transition<Day>) {
    val alpha = dayChangeTransition.animateFloat(
        label = "",
        transitionSpec = { tween(400) }) {
        if (it == NIGHT) 1f else 0f
    }
    val alpha2 = dayChangeTransition.animateFloat(
        label = "",
        transitionSpec = { tween(400) }) {
        if (it == AFTERNOON) 1f else 0f
    }
    Image(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
        painter = painterResource(id = R.drawable.landscape_morning),
        contentDescription = null
    )
    Image(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
        painter = painterResource(id = R.drawable.landscape_afternoon),
        contentDescription = null,
        alpha = alpha2.value
    )
    Image(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
        painter = painterResource(id = R.drawable.landscape_night),
        contentDescription = null,
        alpha = alpha.value
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SentientWeatherAITheme {
        WeatherScreen()
    }
}