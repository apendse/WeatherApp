package com.aap.compose.weatherapp

import androidx.lifecycle.ViewModel
import com.aap.compose.weatherapp.location.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val locationProvider: LocationProvider): ViewModel() {
    fun handleLocationPermissionGranted() {
        locationProvider.setLocationPermissionGranted()
    }
}