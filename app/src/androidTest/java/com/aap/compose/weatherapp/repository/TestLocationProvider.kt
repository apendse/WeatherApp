package com.aap.compose.weatherapp.repository

import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.location.LocationModule
import com.aap.compose.weatherapp.location.LocationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Inject
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocationModule::class]
)
interface TestLocationProviderModule {
    @Singleton
    @Binds
    fun bindConfigRepository(testConfigRepository: TestLocationProvider): LocationProvider

}

class TestLocationProvider @Inject constructor(): LocationProvider {
    override fun setLocationPermissionGranted() {
        // NOOP
    }

    override fun getCurrentLocation(): GeoLocationData {
        return GeoLocationData("aa", 0.0, 0.0, "", "", true)
    }

    override fun isLocationEnabled(): Boolean {
        return true
    }

}