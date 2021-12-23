package hu.friedcoyote.swai.presentation.weather.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import hu.friedcoyote.swai.util.toCityName

@Composable
fun SearchAppBar(
    modifier: Modifier,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    focusRequester: FocusRequester
) {
    var searchText by rememberSaveable {
        mutableStateOf("")
    }
    Row(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester),
            value = searchText,
            onValueChange = {
                searchText = it.toCityName()
            },
            label = null,
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = "Search city",
                    color = Color.White,
                    style = MaterialTheme.typography.subtitle1
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                fontStyle = MaterialTheme.typography.subtitle1.fontStyle,
                textAlign = MaterialTheme.typography.subtitle1.textAlign,
                fontWeight = MaterialTheme.typography.subtitle1.fontWeight,
                letterSpacing = MaterialTheme.typography.subtitle1.letterSpacing,
                lineHeight = MaterialTheme.typography.subtitle1.lineHeight,
                fontFamily = MaterialTheme.typography.subtitle1.fontFamily
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        onCloseClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(searchText)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ))
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler {
        onCloseClicked()
    }
}