package com.aap.compose.weatherapp.data

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("coord") val coordinates: Coordinates,
    val weather: List<WeatherItem>,
    @SerializedName("main") val temperatures: TemperatureData,
    val name: String
)

data class Coordinates(val lat: Double, val lon: Double)

data class WeatherItem(val id: Int, val main: String, val description: String, val icon: String)

data class TemperatureData(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val humidity: Double
)

data class SystemData(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Int,
    val sunset: Int
)