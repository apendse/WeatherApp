package com.aap.compose.weatherapp.repository

import android.content.SharedPreferences
import android.util.Log
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.network.API_KEY
import com.aap.compose.weatherapp.network.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject
import javax.inject.Named

const val DEFAULT_LIMIT = 5
const val LATITUDE_PREF = "latitude"
const val LONGITUDE_PREF = "longitude"
const val NAME_PREF = "NamePref"
const val COUNTRY_PREF = "Country"
const val STATE_PREF = "StatePref"
const val MAX_SIZE = 5

class WeatherRepositoryImpl @Inject constructor(
    val weatherService: WeatherService, val configRepository: ConfigRepository,
    val sharedPreferences: SharedPreferences
) :
    WeatherRepository {

    @Inject
    @JvmField
    @Named(API_KEY)
    var apiKey = ""

    init {
        Log.d("YYYY", "Weather repo created")
    }
    val recents = mutableListOf<GeoLocationData>()

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
        if (recents.contains(geoLocationData)) {
            return
        }
        Log.d("YYYY", "Recents size1 ${recents.size}")
        recents.add(0, geoLocationData)
        Log.d("YYYY", "Recents size2 ${recents.size}")
        while(recents.size > MAX_SIZE) {
            recents.removeLast()
        }
        Log.d("YYYY", "Recents size3 ${recents.size}")
    }

    private fun savePrefs(locationParam: GeoLocationData) {
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

            emit(weatherService.getLatLongForLocation(location, apiKey, DEFAULT_LIMIT))

        }
    }

    override suspend fun getGeoLocationForZip(zip: String): Flow<GeoLocationData> {
        return flow {

            emit(weatherService.getLatLongForZip(zip, apiKey))
        }
    }

    override fun getRecentLocations() = recents
}