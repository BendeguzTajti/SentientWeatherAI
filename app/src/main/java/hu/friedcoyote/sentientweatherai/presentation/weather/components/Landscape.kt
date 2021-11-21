package hu.friedcoyote.sentientweatherai.presentation.weather.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import hu.friedcoyote.sentientweatherai.R
import hu.friedcoyote.sentientweatherai.domain.model.Day

@Composable
fun Landscape(dayChangeTransition: Transition<Day>) {
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