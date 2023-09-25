package com.aap.compose.weatherapp.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.LocationParam
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDetailScreenViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel() {
    private val _state = MutableStateFlow<DetailScreenState>(DetailScreenState.Idle)
    val state: StateFlow<DetailScreenState> = _state

    fun fetchWeatherData(location: LocationParam) {
        _state.value = DetailScreenState.ShowSpinner
        viewModelScope.launch {
            try {
                weatherRepository.getWeatherForLocation(location).collect {
                    _state.value = DetailScreenState.ShowWeather(it)
                }
            } catch (throwable: Throwable) {
                _state.value = DetailScreenState.Error(throwable)
            }
        }
    }
}

sealed class DetailScreenState {
    object Idle : DetailScreenState()
    object ShowSpinner: DetailScreenState()

    class ShowWeather(val weatherData: WeatherData): DetailScreenState()

    class Error(val throwable: Throwable): DetailScreenState()
}