package com.aap.compose.weatherapp.screens.home

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aap.compose.weatherapp.R
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.repository.Units
import com.aap.compose.weatherapp.screens.detail.WeatherScreen

// When we search by zip code, we don't get the state. So this dummy is used to pass state
const val DUMMY_STATE = "dummyState"
@Composable
fun HomeScreen(
    onLocationClicked: (latitude: Double, longitude: Double, name: String, country: String, state: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    Column(
        modifier = modifier
    ) {
        val context = LocalContext.current
        val uiState by viewModel.state.collectAsState()
        WeatherSearchBar(viewModel.recentSearches, onRecentSearchClick = { location -> onLocationClicked(location.lat, location.lon, location.name, location.country, location.state ?: DUMMY_STATE)} ) { query -> viewModel.getGeoLocationFor(query) }
        when (uiState) {
            is HomeState.ErrorState -> {
                val errorString = "Error ${(uiState as HomeState.ErrorState).throwable}"
                Toast.makeText(context, errorString, Toast.LENGTH_LONG).show()
            }

            is HomeState.FetchingDataState -> {
                ShowSpinner()
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
                ShowNoMatchingLocations()
            }

            is HomeState.MultipleMatchingLocations -> {
                val multipleSelection = uiState as HomeState.MultipleMatchingLocations
                ShowListOfLocations(multipleSelection.geoLocationDataList, onLocationClicked)
            }

            is HomeState.ShowWeatherForPreviousLocation -> {
                val showWeather = uiState as HomeState.ShowWeatherForPreviousLocation
                RenderWeatherForLastLocation(showWeather.weatherData, viewModel.getUnits())
            }

            is HomeState.SingleMatchingLocation -> {
                viewModel.resetState()
                navigateToLocationForecast(uiState as HomeState.SingleMatchingLocation, onLocationClicked)
            }
        }
    }
}

@Composable
private fun ShowNoMatchingLocations() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.no_matching_locations),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RenderWeatherForLastLocation(weatherData: WeatherData, units: Units) {
    Column() {
        Spacer(modifier = Modifier.height(4.dp))
        Text(stringResource(id = R.string.last_lcoation_forecast))
        Spacer(modifier = Modifier.height(4.dp))
    }
    WeatherScreen(weatherData = weatherData, units = units)
}

private fun navigateToLocationForecast(
    uiState: HomeState.SingleMatchingLocation,
    onLocationClicked: (latitude: Double, longitude: Double, name: String, country: String, state: String) -> Unit
) {
    with(uiState.geoLocationData) {
        onLocationClicked(lat, lon, name, country, state ?: DUMMY_STATE)
    }
}

/**
 * When user searches for a location and we have multiple loacations with the same name,
 * we show the list and ask user to pick the right location.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowListOfLocations(
    list: List<GeoLocationData>,
    onLocationSelected: (latitude: Double, longitude: Double, name: String, country: String, state: String) -> Unit
) {
    val header = stringResource(id = R.string.location_header)
    LazyColumn() {
        stickyHeader {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray), text = header, color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(list.size) {
            val element = list[it]
            val content = "${element.name}, ${element.state}, ${element.country}"
            Text(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onLocationSelected(
                        element.lat,
                        element.lon,
                        element.name,
                        element.country,
                        element.state ?: DUMMY_STATE
                    )
                }, text = content
            )
        }
    }
}

@Composable
fun ShowSpinner() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}