package hu.friedcoyote.swai.presentation.weather.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hu.friedcoyote.swai.R
import hu.friedcoyote.swai.presentation.weather.WeatherState

@Composable
fun DefaultAppBar(
    modifier: Modifier,
    weatherState: WeatherState,
    onSearchClicked: () -> Unit,
    onMicClicked: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            if (weatherState.isLoading) {
                Text(
                    text = stringResource(R.string.searching),
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.White
                )
            } else {
                Text(
                    text = weatherState.cityName,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        backgroundColor = Color.Transparent,
        contentColor = Color.White,
        elevation = 0.dp,
        actions = {
            IconButton(
                enabled = !weatherState.isLoading,
                onClick = onSearchClicked
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            IconButton(
                enabled = !weatherState.isLoading,
                onClick = onMicClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mic),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}