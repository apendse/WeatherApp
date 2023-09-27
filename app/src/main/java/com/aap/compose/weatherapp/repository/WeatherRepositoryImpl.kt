package com.aap.compose.weatherapp.repository

import android.content.SharedPreferences
import android.util.Log
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.network.API_KEY
import com.aap.compose.weatherapp.network.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject
import javax.inject.Named

const val MAXIMUM_MATCHING_NAMES_LIMIT = 2
const val LATITUDE_PREF = "latitude"
const val LONGITUDE_PREF = "longitude"
const val NAME_PREF = "NamePref"
const val COUNTRY_PREF = "Country"
const val STATE_PREF = "StatePref"
const val MAX_NUM_RECENT_LOCATIONS = 5

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService, val configRepository: ConfigRepository,
    private val sharedPreferences: SharedPreferences
) :
    WeatherRepository {

    @Inject
    @JvmField
    @Named(API_KEY)
    var apiKey = ""

    private val recentLocations = mutableListOf<GeoLocationData>()

    override fun getWeatherForLocation(geoLocationData: GeoLocationData): Flow<WeatherData> {
        return flow {
            val weatherData = weatherService.getWeatherForGeoLocation(
                geoLocationData.lat,
                geoLocationData.lon,
                apiKey,
                configRepository.getUnits().name
            )
            savePrefs(geoLocationData)
            updateRecent(geoLocationData)
            emit(weatherData)
        }
    }

    @VisibleForTesting
    fun updateRecent(geoLocationData: GeoLocationData) {
        if (geoLocationData.isCurrentLocation || recentLocations.contains(geoLocationData)) {
            return
        }
        if (geoLocationData.name == "Current Location") {
            Log.d("YYYY", "Here")
        }
        recentLocations.add(0, geoLocationData)
        while(recentLocations.size > MAX_NUM_RECENT_LOCATIONS) {
            recentLocations.removeLast()
        }
    }

    private fun savePrefs(locationParam: GeoLocationData) {
        if (locationParam.isCurrentLocation) {
            return
        }
        val editor = sharedPreferences.edit()
        editor.putFloat(LATITUDE_PREF, locationParam.lat.toFloat())
        editor.putFloat(LONGITUDE_PREF, locationParam.lon.toFloat())
        editor.putString(NAME_PREF, locationParam.name)
        editor.putString(COUNTRY_PREF, locationParam.country)
        editor.putString(STATE_PREF, locationParam.state)
        editor.apply()

    }

    override fun getLastLocation(): GeoLocationData? {
        val lat = sharedPreferences.getFloat(LATITUDE_PREF, 0f)
        val long = sharedPreferences.getFloat(LONGITUDE_PREF, 0f)
        val name = sharedPreferences.getString(NAME_PREF, "") ?: ""
        val country = sharedPreferences.getString(COUNTRY_PREF, "") ?: ""
        val state = sharedPreferences.getString(STATE_PREF, "") ?: ""
        return if (lat == 0f && long == 0f) {
            null
        } else {
            GeoLocationData(name, lat.toDouble(), long.toDouble(), country, state)
        }

    }

    override suspend fun getGeoLocation(location: String): Flow<List<GeoLocationData>> {

        return flow {

            emit(weatherService.getLatLongForLocation(location, apiKey, MAXIMUM_MATCHING_NAMES_LIMIT))

        }
    }

    override suspend fun getGeoLocationForZip(zip: String): Flow<GeoLocationData> {
        return flow {

            emit(weatherService.getLatLongForZip(zip, apiKey))
        }
    }

    override fun getRecentLocations() = recentLocations
}