package com.aap.compose.weatherapp.location

import com.aap.compose.weatherapp.data.GeoLocationData

interface LocationProvider {
    fun setLocationPermissionGranted()
    fun getCurrentLocation(): GeoLocationData
    fun isLocationEnabled(): Boolean
}