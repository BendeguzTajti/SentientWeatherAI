package hu.friedcoyote.swai.presentation.weather.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hu.friedcoyote.swai.R

@Composable
fun WeatherAppBar(
    modifier: Modifier,
    cityName: String,
    onMicClicked: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = cityName,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        backgroundColor = Color.Transparent,
        contentColor = Color.White,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            IconButton(onClick = onMicClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mic),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}