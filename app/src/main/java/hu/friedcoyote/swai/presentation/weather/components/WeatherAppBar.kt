package hu.friedcoyote.swai.presentation.weather.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import hu.friedcoyote.swai.presentation.weather.SearchWidgetState
import hu.friedcoyote.swai.presentation.weather.WeatherState

@Composable
fun WeatherAppBar(
    statusBarPaddings: PaddingValues,
    weatherState: WeatherState,
    onSearchClicked: (String) -> Unit,
    onMicClicked: () -> Unit
) {
    var searchWidgetState by rememberSaveable {
        mutableStateOf(SearchWidgetState.CLOSED)
    }
    val focusRequester = remember { FocusRequester() }
    when (searchWidgetState) {
        SearchWidgetState.OPENED -> {
            SearchAppBar(
                modifier = Modifier
                    .padding(top = statusBarPaddings.calculateTopPadding())
                    .fillMaxWidth()
                    .height(56.dp),
                onCloseClicked = { searchWidgetState = SearchWidgetState.CLOSED },
                onSearchClicked = {
                    onSearchClicked(it)
                    searchWidgetState = SearchWidgetState.CLOSED
                },
                focusRequester = focusRequester
            )
        }
        SearchWidgetState.CLOSED -> {
            DefaultAppBar(
                modifier = Modifier.padding(top = statusBarPaddings.calculateTopPadding()),
                weatherState = weatherState,
                onSearchClicked = { searchWidgetState = SearchWidgetState.OPENED },
                onMicClicked = onMicClicked
            )
        }
    }
}