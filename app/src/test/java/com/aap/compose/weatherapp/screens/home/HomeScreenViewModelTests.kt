package com.aap.compose.weatherapp.screens.home

import com.aap.compose.weatherapp.data.Coordinates
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.TemperatureData
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.location.LocationProvider
import com.aap.compose.weatherapp.repository.ConfigRepository
import com.aap.compose.weatherapp.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HomeScreenViewModelTests {

    @MockK
    lateinit var weatherRepository: WeatherRepository

    @MockK
    lateinit var configRepository: ConfigRepository

    @MockK
    lateinit var locationProvider: LocationProvider

    lateinit var homeScreenViewModel: HomeScreenViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        homeScreenViewModel =
            HomeScreenViewModel(weatherRepository, configRepository, locationProvider)
    }

    @Test
    fun recentLocations_fetchRecentLocationsCallWithoutCurrent_returnsTheFlow() = runTest {
        val locationList = listOf(
            GeoLocationData("test1", 2.0, 1.0, "USA", "", false),
            GeoLocationData("test2", 1.0, 2.0, "USA", "WA", false),
            GeoLocationData("test3", 4.0, 5.0, "USA", "ID", false)
        )
        every { locationProvider.isLocationEnabled() } returns false
        every { weatherRepository.getRecentLocations() } returns locationList
        homeScreenViewModel.fetchRecentLocationsCall().collect {

            Assert.assertEquals(locationList, it)
        }
    }

    @Test
    fun recentLocations_fetchRecentLocationsCallWithCurrent_returnsTheFlow() = runTest {
        val locationList = listOf(
            GeoLocationData("test1", 2.0, 1.0, "USA", "", false),
            GeoLocationData("test2", 1.0, 2.0, "USA", "WA", false),
            GeoLocationData("test3", 4.0, 5.0, "USA", "ID", false)
        )
        val current = GeoLocationData("Current", 10.0, 11.0, "USA", "VT", true)
        every { locationProvider.isLocationEnabled() } returns true
        every { locationProvider.getCurrentLocation() } returns current

        every { weatherRepository.getRecentLocations() } returns locationList
        homeScreenViewModel.fetchRecentLocationsCall().collect {

            Assert.assertEquals(locationList.size + 1, it.size)
            Assert.assertEquals(it[0], current)
            val sublist = it.subList(1, it.size)
            Assert.assertEquals(locationList, sublist)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModel_onGettingWeatherData_updatesState() = runTest {
        val expected = WeatherData(
            Coordinates(lat = 10.0, lon = 20.0),
            emptyList(),
            mockk<TemperatureData>(),
            "test"
        )
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        every { weatherRepository.getWeatherForLocation(any()) } returns flowOf(expected)
        try {
            homeScreenViewModel.getWeatherForLocation(
                GeoLocationData(
                    "test1",
                    2.0,
                    1.0,
                    "USA",
                    "",
                    false
                )
            )
        } finally {
            Dispatchers.resetMain()
        }
        Assert.assertTrue(homeScreenViewModel.state.value is  HomeState.ShowWeatherForPreviousLocation)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModel_onGettingErrorFetchingWeather_updatesState() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        val exception = RuntimeException("Test")
        every { weatherRepository.getWeatherForLocation(any()) } throws exception
        try {
            homeScreenViewModel.getWeatherForLocation(
                GeoLocationData(
                    "test1",
                    2.0,
                    1.0,
                    "USA",
                    "",
                    false
                )
            )
        } finally {
            Dispatchers.resetMain()
        }
        Assert.assertTrue(homeScreenViewModel.state.value is  HomeState.ErrorState)
        Assert.assertEquals(exception, (homeScreenViewModel.state.value as HomeState.ErrorState).throwable)

    }


}