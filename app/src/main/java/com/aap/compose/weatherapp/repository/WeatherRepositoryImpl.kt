package com.aap.compose.weatherapp.repository

import android.content.SharedPreferences
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.LocationParam
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.network.API_KEY
import com.aap.compose.weatherapp.network.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

const val DEFAULT_LIMIT = 5
const val LATITUDE_PREF = "latitude"
const val LONGITUDE_PREF = "longitude"
class WeatherRepositoryImpl @Inject constructor(val weatherService: WeatherService, val configRepository: ConfigRepository,
        val sharedPreferences: SharedPreferences) :
    WeatherRepository {

    @Inject
    @JvmField
    @Named(API_KEY)
    var apiKey = ""

    override fun getWeatherForLocation(locationParam: LocationParam): Flow<WeatherData> {
        return flow {
            val weatherData = weatherService.getWeatherForGeoLocation(
                locationParam.latitude,
                locationParam.longitude,
                apiKey,
                configRepository.getUnits().name)
            savePrefs(locationParam)
            emit(weatherData)
        }
    }

    private fun savePrefs(locationParam: LocationParam) {
        val editor = sharedPreferences.edit()
        editor.putFloat(LATITUDE_PREF, locationParam.latitude.toFloat())
        editor.putFloat(LONGITUDE_PREF, locationParam.longitude.toFloat())
        editor.commit()

    }

    override fun getLastLocation(): LocationParam? {
        val lat = sharedPreferences.getFloat(LATITUDE_PREF, 0f)
        val long = sharedPreferences.getFloat(LONGITUDE_PREF, 0f)
        return if (lat == 0f && long == 0f) {
            null
        } else {
            LocationParam(lat.toDouble(), long.toDouble())
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
}