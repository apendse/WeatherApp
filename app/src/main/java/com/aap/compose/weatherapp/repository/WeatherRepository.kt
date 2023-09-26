package com.aap.compose.weatherapp.repository

import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherForLocation(geoLocationData: GeoLocationData): Flow<WeatherData>

    fun getLastLocation(): GeoLocationData?

    suspend fun getGeoLocation(location: String): Flow<List<GeoLocationData>>

    suspend fun getGeoLocationForZip(zip: String): Flow<GeoLocationData>

    fun getRecentLocations(): List<GeoLocationData>
}