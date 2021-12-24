package hu.friedcoyote.swai.presentation.weather.components

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import hu.friedcoyote.swai.R
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.domain.model.Weather

@ExperimentalAnimationGraphicsApi
@Composable
fun CurrentWeather(modifier: Modifier, currentWeather: Weather?, dayType: DayType) {
    val image = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = R.drawable.landscape
        ),
        atEnd = dayType == DayType.NIGHT)
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (temperature, description, windAndHumidity, cloudsAndPop, landscape) = createRefs()
        if (currentWeather != null) {
            Text(
                modifier = Modifier.constrainAs(temperature) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 16.dp)
                },
                text = "${currentWeather.temperatureCelsius}°ᶜ",
                color = Color.White,
                style = MaterialTheme.typography.h3
            )
            Text(
                modifier = Modifier.constrainAs(description) {
                    top.linkTo(temperature.bottom, 4.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(windAndHumidity.start, 8.dp)
                    width = Dimension.fillToConstraints
                },
                text = currentWeather.description,
                color = Color.White,
                style = MaterialTheme.typography.body1
            )
            Column(
                modifier = Modifier.constrainAs(windAndHumidity) {
                    top.linkTo(temperature.top, 10.dp)
                    end.linkTo(cloudsAndPop.start, 8.dp)
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(
                        id = if (currentWeather.snowPop > 0) R.drawable.ic_snow else R.drawable.ic_rain
                    ),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${if (currentWeather.snowPop > 0) currentWeather.snowPop else currentWeather.snowPop} mm",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_squall),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${currentWeather.windSpeed} km/h",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                    color = Color.White
                )
            }
            Column(
                modifier = Modifier.constrainAs(cloudsAndPop) {
                    top.linkTo(temperature.top, 10.dp)
                    end.linkTo(parent.end, 16.dp)
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_cloud),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${currentWeather.cloudsPercent}%",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_humidity),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${currentWeather.humidityPercent}%",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                    color = Color.White
                )
            }
        }
        Image(
            modifier = Modifier
                .constrainAs(landscape) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}