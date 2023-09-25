package com.aap.compose.weatherapp.screens.home

import com.aap.compose.weatherapp.R
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.screens.detail.WeatherScreen

@Composable
fun HomeScreen(
    onLocationClicked: (latitude: Double, longitude: Double) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    Column(
        modifier = modifier
    ) {
        val context = LocalContext.current
        val uiState by viewModel.state.collectAsState()
        WeatherSearchBar { query -> viewModel.getGeoLocationFor(query) }
        when (uiState) {
            is HomeState.ErrorState -> {
                val errorString = "Error ${(uiState as HomeState.ErrorState).throwable}"
                Toast.makeText(context, errorString, Toast.LENGTH_LONG).show()
            }

            is HomeState.FetchingDataState -> {
                showSpinner()
            }

            is HomeState.CheckIfPreviousLocationAvailable -> {
                if (viewModel.hasLastLocation()) {
                    viewModel.loadWeatherForLastLocation()
                } else {
                    viewModel.resetState()
                }
            }

            is HomeState.IdleState -> {

            }

            is HomeState.NoMatchingLocations -> {
                showNoMatchingLocations()
            }
            is HomeState.MultipleMatchingLocations -> {
                val multipleSelection = uiState as HomeState.MultipleMatchingLocations
                ShowListOfLocations(multipleSelection.geoLocationDataList, onLocationClicked)
            }

            is HomeState.ShowWeatherForPreviousLocation -> {
                val showWeather = uiState as HomeState.ShowWeatherForPreviousLocation
                renderWeather(showWeather.weatherData)
            }
            is HomeState.SingleMatchingLocation -> {
                viewModel.resetState()
                navigateToDetail(uiState as HomeState.SingleMatchingLocation, onLocationClicked)
            }
        }
    }
}

@Composable
private fun showNoMatchingLocations() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.no_matching_locations), fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun renderWeather(weatherData: WeatherData) {
    WeatherScreen(weatherData = weatherData)
}

private fun navigateToDetail(uiState: HomeState.SingleMatchingLocation, onLocationClicked: (latitude: Double, longitude: Double) -> Unit) {
    onLocationClicked(uiState.geoLocationData.lat, uiState.geoLocationData.lon)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowListOfLocations(
    list: List<GeoLocationData>,
    onLocationSelected: (latitude: Double, longitude: Double) -> Unit
) {
    val header = stringResource(id = R.string.location_header)
    LazyColumn() {
        stickyHeader {
            Spacer(modifier = Modifier.height(4.dp))
            Text(modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray), text = header, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(list.size) {
            val element = list[it]
            val content = "${element.name}, ${element.state}, ${element.country}"
            Text(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onLocationSelected(element.lat, element.lon)
                }, text = content)
        }
    }
}

@Composable
fun showSpinner() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}