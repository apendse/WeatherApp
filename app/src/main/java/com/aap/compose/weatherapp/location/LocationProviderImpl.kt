package com.aap.compose.weatherapp.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.aap.compose.weatherapp.R
import com.aap.compose.weatherapp.data.GeoLocationData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val DUMMY_COUNTRY = "PlaceHolderCountry"
const val DUMMY_STATE = "PlaceHolderState"
class LocationProviderImpl @Inject constructor(@ApplicationContext private val  context: Context): LocationProvider {

    private var locationPermissionGrantedFlag: Boolean = false
    override fun setLocationPermissionGranted() {
        locationPermissionGrantedFlag = true
    }

    override fun isLocationEnabled(): Boolean {
        return locationPermissionGrantedFlag
    }

    override fun getCurrentLocation(): GeoLocationData {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var geoLocationData: GeoLocationData? = null
        providers.forEach {

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                throw RuntimeException("No permission to get network")
            }

            val location = locationManager.getLastKnownLocation(it)
            if (location != null) {
                geoLocationData = GeoLocationData(context.getString(R.string.current_location), location.latitude, location.longitude, DUMMY_COUNTRY, DUMMY_STATE, isCurrentLocation = true)
            }
        }
        return geoLocationData ?: GeoLocationData("", 0.0, 0.0, "", "")
    }
}