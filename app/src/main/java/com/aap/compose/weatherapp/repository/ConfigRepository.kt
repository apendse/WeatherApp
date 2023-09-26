package com.aap.compose.weatherapp.repository

interface ConfigRepository {
    fun getUnits(): Units
    fun setUnits(units: Units)
}

enum class Units(value: String) {
    STANDARD("standard"),
    METRIC("metric"),
    IMPERIAL("imperial"),
}