package com.aap.compose.weatherapp.repository

import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.LocationParam
import com.aap.compose.weatherapp.data.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherForLocation(locationParam: LocationParam): Flow<WeatherData>

    fun getLastLocation(): LocationParam?

    suspend fun getGeoLocation(location: String): Flow<List<GeoLocationData>>

    suspend fun getGeoLocationForZip(zip: String): Flow<GeoLocationData>
}