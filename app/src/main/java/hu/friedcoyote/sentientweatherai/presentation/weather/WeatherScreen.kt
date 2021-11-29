package hu.friedcoyote.sentientweatherai.presentation.weather

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.text.format.DateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.friedcoyote.sentientweatherai.R
import hu.friedcoyote.sentientweatherai.presentation.weather.components.ForecastListItem
import hu.friedcoyote.sentientweatherai.presentation.weather.components.Landscape
import kotlinx.coroutines.FlowPreview
import java.text.SimpleDateFormat
import java.util.*

@FlowPreview
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            val recognizedWords =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result.resultCode == Activity.RESULT_OK && !recognizedWords.isNullOrEmpty()) {
                viewModel.getWeatherByCityName(recognizedWords.first())
            }
        }
    val scaffoldState = rememberScaffoldState()
    val pattern = if (DateFormat.is24HourFormat(LocalContext.current)) "HH:mm" else "hh:mm"
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    val weatherState = viewModel.weatherState.value
    val transition = updateTransition(viewModel.dayType.value, label = "dayChangeTransition")
    val searchError = viewModel.searchError.collectAsState(initial = null)
    if (searchError.value != null) {
        LaunchedEffect(searchError.value) {
            scaffoldState.snackbarHostState.showSnackbar(
                "No results found"
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.7f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Landscape(dayChangeTransition = transition)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (weatherState.weather != null) {
                        Text(
                            modifier = Modifier.padding(top = 62.dp, start = 24.dp),
                            text = "${weatherState.weather.currentWeather?.temperatureCelsius}°",
                            color = Color.White,
                            style = MaterialTheme.typography.h1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 18.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    FloatingActionButton(
                        onClick = {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                )
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
                            }
                            speechRecognizerLauncher.launch(intent)
                        },
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mic),
                            contentDescription = "Localized description"
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(start = 18.dp, end = 18.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                if (weatherState.weather != null) {
                    dateFormat.timeZone = TimeZone.getTimeZone(weatherState.weather.zoneId)
                    Text(
                        modifier = Modifier.padding(bottom = 12.dp),
                        text = "Today",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.SemiBold
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        items(weatherState.weather.hourlyForecasts) { forecast ->
                            ForecastListItem(
                                dateFormat = dateFormat,
                                forecast = forecast
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                }
            }
        }
    }
}