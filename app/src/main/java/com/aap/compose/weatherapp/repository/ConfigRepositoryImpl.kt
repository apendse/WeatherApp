package com.aap.compose.weatherapp.repository

import javax.inject.Inject

/**
 * Configuration of parameters required for the app.
 * Things like units of temperature, how many recent locations do you want to remember
 * Also, disable current location weather flag can be added
 * Language etc can be added.
 */
class ConfigRepositoryImpl @Inject constructor() : ConfigRepository {
    private var units: Units = Units.IMPERIAL
    override fun getUnits(): Units {
        return units
    }

    override fun setUnits(units: Units) {
        this.units = units
    }
}