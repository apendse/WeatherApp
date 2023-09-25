package com.aap.compose.weatherapp.repository

import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(): ConfigRepository {
    private var units: Units = Units.IMPERIAL
    override fun getUnits(): Units {
        return units
    }

    override fun setUnits(units: Units) {
         this.units = units
    }
}