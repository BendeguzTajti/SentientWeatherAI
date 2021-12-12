package hu.friedcoyote.swai.presentation.weather.components

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import hu.friedcoyote.swai.R
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.model.Weather

@ExperimentalAnimationGraphicsApi
@Composable
fun CurrentWeather(modifier: Modifier, currentWeather: Weather?) {
    val image = animatedVectorResource(id = R.drawable.landscape)
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (temperature, description, landscape) = createRefs()
        Text(
            modifier = Modifier.constrainAs(temperature) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = "${currentWeather?.temperatureCelsius}°ᶜ",
            color = Color.White,
            style = MaterialTheme.typography.h3
        )
        Text(
            modifier = Modifier.constrainAs(description) {
                top.linkTo(temperature.bottom, 4.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = currentWeather?.description ?: "",
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Image(
            modifier = Modifier
                .constrainAs(landscape) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth(),
            painter = image.painterFor(atEnd = currentWeather?.dayType == DayType.NIGHT),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}