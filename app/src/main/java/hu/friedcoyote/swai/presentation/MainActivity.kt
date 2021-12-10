package hu.friedcoyote.swai.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import hu.friedcoyote.swai.presentation.ui.theme.SWAITheme
import hu.friedcoyote.swai.presentation.weather.WeatherScreen

@ExperimentalAnimationGraphicsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SWAITheme {
                WeatherScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SWAITheme {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (currentWeather, tabRow, forecastWeather) = createRefs()
            val guideline = createGuidelineFromBottom(0.2f)
            Box(modifier = Modifier
                .background(Color.Green)
                .constrainAs(currentWeather) {
                    top.linkTo(parent.top)
                    bottom.linkTo(tabRow.top)
                    height = Dimension.fillToConstraints
                }.fillMaxWidth())
            Box(modifier = Modifier
                .background(Color.Blue)
                .constrainAs(tabRow) {
                    bottom.linkTo(forecastWeather.top)
                    height = Dimension.value(75.dp)
                }
                .fillMaxWidth())
            Box(modifier = Modifier
                .background(Color.Red)
                .constrainAs(forecastWeather) {
                    top.linkTo(guideline)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth())
        }
    }
}