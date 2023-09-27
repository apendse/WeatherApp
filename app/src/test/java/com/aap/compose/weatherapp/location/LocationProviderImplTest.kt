package com.aap.compose.weatherapp.location

import android.content.Context
import android.location.Location
import android.location.LocationManager
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LocationProviderImplTest {

    @MockK(relaxed = true)
    lateinit var context: Context

    lateinit var locationProviderImpl: LocationProviderImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        locationProviderImpl = LocationProviderImpl(context)
    }

    @Test
    fun locationProvider_happyPath_returnsGoodLocation() {
        val lat = 3.1412
        val lon = 2.71828
        val mockLocationManger = mockk<LocationManager>()
        val mockLocation = mockk<Location>()
        every {mockLocationManger.getLastKnownLocation(any()) } returns mockLocation
        every { context.getSystemService(any())}  returns mockLocationManger
        every {mockLocationManger.getProviders(true)} returns listOf("mockProvider")
        every {mockLocation.latitude } returns lat
        every {mockLocation.longitude } returns lon
        val location = locationProviderImpl.getCurrentLocation()
        Assert.assertEquals(location.lat, lat, 0.0)
        Assert.assertEquals(location.lon, lon, 0.0)
        Assert.assertEquals(location.isCurrentLocation, true)
    }
}