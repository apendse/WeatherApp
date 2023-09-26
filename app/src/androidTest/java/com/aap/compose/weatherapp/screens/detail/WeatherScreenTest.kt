package com.aap.compose.weatherapp.screens.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.aap.compose.weatherapp.data.Coordinates
import com.aap.compose.weatherapp.data.TemperatureData
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.data.WeatherItem
import com.aap.compose.weatherapp.repository.Units
import org.junit.Rule
import org.junit.Test

class WeatherScreenTest {
    @get :Rule
    val composeTestRule = createComposeRule()

    @Test
    fun weatherScreen_afterRender_validateData() {
        val weatherData = WeatherData(Coordinates(1.0, 2.0), listOf(WeatherItem(100, "Milpitas", "Mostly Sunny", "40d")),
                        TemperatureData(80.1, 82.0, 55.0, 83.0, 65.0), "Milpitas")
        val units = Units.IMPERIAL
        composeTestRule.setContent {
            WeatherScreen(weatherData = weatherData, units = units)
        }
        val humidityString = "${weatherData.temperatures.humidity}"

        composeTestRule.onNodeWithText(weatherData.temperatures.temp.toString(), substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherData.temperatures.tempMin.toString(), substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherData.temperatures.tempMax.toString(), substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherData.temperatures.feelsLike.toString(), substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(humidityString, substring = true).assertIsDisplayed()
        val weatherItem = weatherData.weather[0]
        composeTestRule.onNodeWithText(weatherItem.description).assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherItem.main).assertIsDisplayed()

    }
}