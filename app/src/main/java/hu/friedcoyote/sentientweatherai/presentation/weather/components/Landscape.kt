package hu.friedcoyote.sentientweatherai.presentation.weather.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import hu.friedcoyote.sentientweatherai.R

@ExperimentalAnimationGraphicsApi
@Composable
fun Landscape(dayChangeTransition: Transition<Boolean>, isNightTime: Boolean) {
    val image = animatedVectorResource(id = R.drawable.landscape)
    val skyColor = dayChangeTransition.animateColor(
        label = "skyColorAnimation",
        transitionSpec = { tween(400) }) { isNight ->
        if (isNight) {
            Color(0xFF6963B8)
        } else {
            Color(0xFF52DCFF)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 28.dp)
            .background(color = skyColor.value),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = image.painterFor(atEnd = isNightTime),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}