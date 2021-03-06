package hu.friedcoyote.swai.presentation.weather

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.speech.RecognizerIntent
import android.text.format.DateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import hu.friedcoyote.swai.R
import hu.friedcoyote.swai.domain.model.DayType
import hu.friedcoyote.swai.presentation.weather.components.*
import hu.friedcoyote.swai.util.isPermanentlyDenied
import java.time.format.DateTimeFormatter
import java.util.*

@ExperimentalPermissionsApi
@ExperimentalAnimationGraphicsApi
@Composable
fun WeatherScreen(
    statusBarPaddings: PaddingValues,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val activity = (LocalContext.current as? Activity)
    val orientation = LocalConfiguration.current.orientation
    val scaffoldState = rememberScaffoldState()
    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            val recognizedWords =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result.resultCode == Activity.RESULT_OK && !recognizedWords.isNullOrEmpty()) {
                viewModel.getWeather(recognizedWords.first(), 500)
            }
        }
    val extraPrompt = stringResource(R.string.speech_recognition_extra_prompt)
    val weatherState by viewModel.weatherState
    val dayType by viewModel.dayType
    val dayChangeTransition = updateTransition(
        targetState = dayType,
        label = "dayChangeTransition"
    )
    val backgroundColor = dayChangeTransition.animateColor(
        label = "skyColorAnimation",
        transitionSpec = { tween(durationMillis = 300, easing = LinearEasing) }) { type ->
        if (type == DayType.NIGHT) {
            Color(0xFF6963B8)
        } else {
            Color(0xFF65C2F5)
        }
    }
    var tabRowState by rememberSaveable { mutableStateOf(0) }
    val titles = stringArrayResource(id = R.array.forecast_types)
    val pattern = if (DateFormat.is24HourFormat(LocalContext.current)) "HH:mm" else "hh:mm"
    val searchError = viewModel.searchError.collectAsState(initial = null)
    if (weatherState.initErrorResId != null) {
        val message = stringResource(weatherState.initErrorResId!!)
        val actionLabel = stringResource(R.string.retry)
        LaunchedEffect(weatherState.initErrorResId) {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Indefinite
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                viewModel.getWeatherByUserLocation()
            }
        }
    }
    if (searchError.value != null) {
        val message = stringResource(searchError.value!!)
        LaunchedEffect(searchError.value) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = message
            )
        }
    }
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    when {
        locationPermissionState.hasPermission -> {
            viewModel.onLocationPermissionGranted()
        }
        locationPermissionState.shouldShowRationale -> {
            PermissionDialog(
                title = stringResource(R.string.permission_required),
                message = stringResource(R.string.location_permission_rationale),
                confirmButtonText = stringResource(R.string.allow),
                onConfirm = {
                    locationPermissionState.launchPermissionRequest()
                },
                dismissButton = {
                    TextButton(
                        onClick = { activity?.finishAffinity() }
                    ) {
                        Text(stringResource(R.string.deny))
                    }
                }
            )
        }
        !locationPermissionState.permissionRequested -> {
            PermissionDialog(
                title = stringResource(R.string.permission_required),
                message = stringResource(R.string.location_permission_rationale),
                confirmButtonText = stringResource(R.string.allow),
                onConfirm = {
                    locationPermissionState.launchPermissionRequest()
                },
                dismissButton = {
                    TextButton(
                        onClick = { activity?.finishAffinity() }
                    ) {
                        Text(stringResource(R.string.deny))
                    }
                }
            )
        }
        locationPermissionState.isPermanentlyDenied() -> {
            PermissionDialog(
                title = stringResource(R.string.permission_required),
                message = stringResource(R.string.location_permission_permanently_denied),
                confirmButtonText = stringResource(R.string.ok),
                onConfirm = {
                    activity?.finishAffinity()
                }
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = backgroundColor.value,
        topBar = {
            WeatherAppBar(
                statusBarPaddings = statusBarPaddings,
                weatherState = weatherState,
                onSearchClicked = {
                    if (it.isNotBlank()) {
                        viewModel.getWeather(it, 500)
                    }
                },
                onMicClicked = {
                    val intent =
                        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                            )
                            putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE,
                                Locale.getDefault()
                            )
                            putExtra(
                                RecognizerIntent.EXTRA_PROMPT,
                                extraPrompt
                            )
                            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                        }
                    speechRecognizerLauncher.launch(intent)
                }
            )
        }
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (currentWeatherContainer, tabRow, forecastWeather) = createRefs()
            val guideline = createGuidelineFromBottom(
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.35f else 0.23f
            )
            CurrentWeather(
                orientation = orientation,
                modifier = Modifier
                    .constrainAs(currentWeatherContainer) {
                        top.linkTo(parent.top)
                        bottom.linkTo(tabRow.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    },
                currentWeather = weatherState.currentWeather,
                dayType = dayType
            )
            TabRow(
                modifier = Modifier
                    .constrainAs(tabRow) {
                        bottom.linkTo(forecastWeather.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.wrapContent
                        width = Dimension.fillToConstraints
                    },
                selectedTabIndex = tabRowState,
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabRowState == index,
                        onClick = {
                            tabRowState = index
                        }
                    )
                }
            }
            LazyRow(
                modifier = Modifier
                    .constrainAs(forecastWeather) {
                        top.linkTo(guideline)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .background(MaterialTheme.colors.surface),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(14.dp)
            ) {
                if (tabRowState == 0) {
                    val hourFormatter = DateTimeFormatter.ofPattern(pattern)
                    items(weatherState.hourlyForecast) { forecast ->
                        ForecastListItem(
                            orientation = orientation,
                            dateFormatter = hourFormatter,
                            forecast = forecast
                        )
                    }
                } else {
                    val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
                    items(weatherState.dailyForecast) { forecast ->
                        ForecastListItem(
                            orientation = orientation,
                            dateFormatter = dayFormatter,
                            forecast = forecast
                        )
                    }
                }
            }
        }
    }
}