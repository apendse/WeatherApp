package com.aap.compose.weatherapp.data

data class GeoLocationData(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String?
)
