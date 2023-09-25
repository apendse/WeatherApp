package com.aap.compose.weatherapp.repository

interface ConfigRepository {
    fun getUnits(): Units
    fun setUnits(units: Units)
}

enum class Units(value: String) {
    STANDARD("standar"),
    METRIC("metric"),
    IMPERIAL("imperial"),
}