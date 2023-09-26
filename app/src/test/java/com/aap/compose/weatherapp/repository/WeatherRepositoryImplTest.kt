package com.aap.compose.weatherapp.repository

import android.content.SharedPreferences
import com.aap.compose.weatherapp.data.Coordinates
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.TemperatureData
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.network.WeatherService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    @MockK
    lateinit var weatherService: WeatherService

    @MockK
    lateinit var configRepository: ConfigRepository

    @MockK
    lateinit var sharedPreferences: SharedPreferences

    lateinit var weatherRepositoryImpl: WeatherRepositoryImpl
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        weatherRepositoryImpl = WeatherRepositoryImpl(weatherService, configRepository, sharedPreferences)
    }

    @Test
    fun getWeatherForLocation_happyPath_dataIsCorrect() = runTest {
        val expected = getWeatherData()
        coEvery { weatherService.getWeatherForGeoLocation(any(), any(), any(), any()) } returns expected
        val editor = mockk<SharedPreferences.Editor>()
        every { configRepository.getUnits() } returns Units.IMPERIAL
        every { sharedPreferences.edit() } returns editor
        every { editor.putFloat(any(), any())} returns editor
        every { editor.commit() } returns true
        val location = GeoLocationData("",0.0, 0.0, "", "")
        val actual = weatherRepositoryImpl.getWeatherForLocation(location).last()

        Assert.assertEquals(expected, actual)
        verify {editor.putFloat(LATITUDE_PREF, 0.0.toFloat())}
        verify {editor.putFloat(LONGITUDE_PREF, 0.0.toFloat())}
    }

    @Test
    fun getWeatherForLocation_happyPath_locationIsStored() = runTest {
        val expected = getWeatherData()
        coEvery { weatherService.getWeatherForGeoLocation(any(), any(), any(), any()) } returns expected
        val editor = mockk<SharedPreferences.Editor>()
        every { configRepository.getUnits() } returns Units.IMPERIAL
        every { sharedPreferences.edit() } returns editor
        every { editor.putFloat(any(), any())} returns editor
        every { editor.commit() } returns true
        val location = GeoLocationData("Lodi", 4.0, 5.0, "USA", "CA")
        weatherRepositoryImpl.getWeatherForLocation(location).last()

        verify {editor.putFloat(LATITUDE_PREF, 4.0.toFloat())}
        verify {editor.putFloat(LONGITUDE_PREF, 5.0.toFloat())}
    }

    @Test
    fun getGeoLocationForZip_happyPath_getsCorrectData() = runTest {
        val expected = GeoLocationData("test", 1.0, 3.0, "USA", "CA")
        coEvery { weatherService.getLatLongForZip(any(), any())} returns  expected

        val actual = weatherRepositoryImpl.getGeoLocationForZip("95035").last()

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getGeoLocation_happyPath_hasCorrectData() = runTest {
        val location = "Milpitas"
        val expected = listOf(GeoLocationData(location, 1.0, 2.0, "USA", "CA"),
                            GeoLocationData(location, 3.0, 4.0, "Mexico", "Baja"))
        coEvery {weatherService.getLatLongForLocation(any(), any(), any())} returns expected

        val actual = weatherRepositoryImpl.getGeoLocation(location).last()

        Assert.assertEquals(expected, actual)

    }
}

private fun getWeatherData(): WeatherData {
    return WeatherData(Coordinates(lat = 1.0, lon = 2.0), emptyList(), mockk<TemperatureData>(), "test")
}
