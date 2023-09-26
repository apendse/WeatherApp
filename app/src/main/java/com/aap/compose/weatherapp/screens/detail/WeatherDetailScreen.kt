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
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.repository.Units
import com.aap.compose.weatherapp.screens.home.ShowSpinner

@Composable
fun WeatherDetailScreen(
    modifier: Modifier,
    geoLocationData: GeoLocationData,
    viewModel: WeatherDetailScreenViewModel = hiltViewModel()
) {
    if (geoLocationData.lat == 0.0 && geoLocationData.lon == 0.0) {
        throw RuntimeException("Invalid lat long")
    }
    val uiState by viewModel.state.collectAsState()
    val context = LocalContext.current
    when (uiState) {
        is DetailScreenState.Error -> {
            val errorString = "Error ${(uiState as DetailScreenState.Error).throwable}"
            Toast.makeText(context, errorString, Toast.LENGTH_LONG).show()

        }

        DetailScreenState.Idle -> {
            viewModel.fetchWeatherData(geoLocationData)
        }

        DetailScreenState.ShowSpinner -> {
            ShowSpinner()
        }

        is DetailScreenState.ShowWeather -> {
            val detail = uiState as DetailScreenState.ShowWeather
            ShowWeatherScreen(detail.weatherData, viewModel.getUnits())
        }
    }
}

@Composable
private fun ShowWeatherScreen(weatherData: WeatherData, units: Units) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        WeatherScreen(modifier = Modifier, weatherData, units)
    }
}
