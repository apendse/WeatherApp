package com.aap.compose.weatherapp.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.location.LocationProvider
import com.aap.compose.weatherapp.repository.ConfigRepository
import com.aap.compose.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val USA_COUNTRY_CODE = "us"

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val weatherRepository: WeatherRepository, private val configRepository: ConfigRepository, private val locationProvider: LocationProvider) :
    ViewModel() {


    private val _state = MutableStateFlow<HomeState>(HomeState.CheckIfPreviousLocationAvailable)
    val state: StateFlow<HomeState> = _state

    val recentSearches
        get() =
            getRecentSearchesWithLocation()

    private fun getRecentSearchesWithLocation(): List<GeoLocationData> {
        return if (locationProvider.isLocationEnabled()) {
            val currentLocation = locationProvider.getCurrentLocation()
            if (currentLocation.isCurrentLocation) {
                mutableListOf<GeoLocationData>().apply {
                    add(currentLocation)
                    addAll(weatherRepository.getRecentLocations())
                }
            } else {
                weatherRepository.getRecentLocations()
            }
        } else {
            weatherRepository.getRecentLocations()
        }
    }

    fun getUnits() = configRepository.getUnits()

    fun resetState() {
        _state.value = HomeState.IdleState
    }

    fun loadWeatherForLastLocation() {
        val locationParam = weatherRepository.getLastLocation() ?: return
        getWeatherForLocation(locationParam)
    }

    private fun getWeatherForLocation(locationData: GeoLocationData) {
        _state.value = HomeState.FetchingDataState
        viewModelScope.launch {
            try {
                weatherRepository.getWeatherForLocation(locationData).collect {
                    _state.value = HomeState.ShowWeatherForPreviousLocation(it)
                }
            } catch (throwable: Throwable) {
                _state.value = HomeState.ErrorState(throwable)
            }
        }

    }

    fun getGeoLocationFor(location: String) {
        _state.value = HomeState.FetchingDataState
        viewModelScope.launch {
            try {
                if (location.isZipCode()) {
                    getGeoLocationForZipCode(location)
                } else {

                    weatherRepository.getGeoLocation(location).collect {
                        if (it.isEmpty()) {
                            _state.value = HomeState.NoMatchingLocations
                        } else if (it.size == 1) {
                            _state.value = HomeState.SingleMatchingLocation(it[0])
                        } else {
                            _state.value = HomeState.MultipleMatchingLocations(it)
                        }
                    }
                }
            } catch (throwable: Throwable) {
                _state.value = HomeState.ErrorState(throwable)
            }
        }


    }

    private suspend fun getGeoLocationForZipCode(zip: String) {
        val zipWithCountry = "$zip,$USA_COUNTRY_CODE"  // default country usa
        weatherRepository.getGeoLocationForZip(zipWithCountry).collect {
            _state.value = HomeState.SingleMatchingLocation(it)
        }

    }


    private fun String.isZipCode() = Regex("^\\d{5}").matches(this)

    /**
     * If there is a stored location
     */
    fun hasLastLocation() = weatherRepository.getLastLocation() != null
}

sealed class HomeState {
    object IdleState : HomeState()
    object CheckIfPreviousLocationAvailable : HomeState()
    object FetchingDataState : HomeState()
    object NoMatchingLocations : HomeState()
    class MultipleMatchingLocations(val geoLocationDataList: List<GeoLocationData>) : HomeState()
    class SingleMatchingLocation(val geoLocationData: GeoLocationData) : HomeState()
    class ShowWeatherForPreviousLocation(val weatherData: WeatherData) : HomeState()
    class ErrorState(val throwable: Throwable) : HomeState()
}