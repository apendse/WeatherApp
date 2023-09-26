package com.aap.compose.weatherapp.repository

import com.aap.compose.weatherapp.data.Coordinates
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.TemperatureData
import com.aap.compose.weatherapp.data.WeatherData
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [WeatherRepositoryModule::class]
)
interface TestWeatherRepositoryModule {
    @Singleton
    @Binds
    fun bindWeatherRepository(
        testWeatherRepository : TestWeatherRepository
    ): WeatherRepository

}

class TestWeatherRepository @Inject constructor():  WeatherRepository {

    var _lastLocation: GeoLocationData? = null
    override fun getWeatherForLocation(geoLocationData: GeoLocationData): Flow<WeatherData> {
        return flow {
            WeatherData(Coordinates(0.0, 0.0), emptyList(), TemperatureData(0.0, 0.0, 0.0, 0.0, 0.0), "")
        }
    }

    override fun getLastLocation(): GeoLocationData? {
        return _lastLocation
    }

    override suspend fun getGeoLocation(location: String): Flow<List<GeoLocationData>> {
        return flow {
            emit(emptyList<GeoLocationData>())
        }
    }

    override suspend fun getGeoLocationForZip(zip: String): Flow<GeoLocationData> {
        return flow {
            emit(GeoLocationData("", 0.0, 0.0, "", ""))
        }
    }

    override fun getRecentLocations(): List<GeoLocationData> {
        return emptyList()
    }

}