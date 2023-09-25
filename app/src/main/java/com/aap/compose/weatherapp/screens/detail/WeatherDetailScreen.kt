package com.aap.compose.weatherapp.screens.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aap.compose.weatherapp.data.LocationParam
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.screens.home.showSpinner

@Composable
fun WeatherDetailScreen(modifier: Modifier, location: LocationParam, viewModel: WeatherDetailScreenViewModel = hiltViewModel()) {
    if (location.latitude == 0.0 && location.longitude == 0.0) {
        throw RuntimeException("Invalid lat long")
    }
    val uiState by viewModel.state.collectAsState()
    val context = LocalContext.current
    when(uiState) {
        is DetailScreenState.Error -> {
            val errorString = "Error ${(uiState as DetailScreenState.Error).throwable}"
            Toast.makeText(context, errorString, Toast.LENGTH_LONG).show()

        }
        DetailScreenState.Idle -> {
            viewModel.fetchWeatherData(location)
        }
        DetailScreenState.ShowSpinner -> {
            showSpinner()
        }
        is DetailScreenState.ShowWeather -> {
            val detail = uiState as DetailScreenState.ShowWeather
            showWeatherScreen(detail.weatherData)
        }
    }
}

@Composable
private fun showWeatherScreen(weatherData: WeatherData) {
    Box(modifier = Modifier.fillMaxSize().padding(top=40.dp), contentAlignment = Alignment.Center) {
        WeatherScreen(modifier = Modifier, weatherData)
    }
}
