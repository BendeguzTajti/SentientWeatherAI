package hu.friedcoyote.swai.presentation.weather.components

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import hu.friedcoyote.swai.R
import hu.friedcoyote.swai.domain.model.DayType

@ExperimentalAnimationGraphicsApi
@Composable
fun CurrentWeather(modifier: Modifier, dayType: DayType) {
    val image = animatedVectorResource(id = R.drawable.landscape)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = image.painterFor(atEnd = dayType == DayType.NIGHT),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}