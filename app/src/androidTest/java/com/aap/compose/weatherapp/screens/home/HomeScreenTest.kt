package com.aap.compose.weatherapp.screens.home

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.aap.compose.weatherapp.R
import com.aap.compose.weatherapp.repository.ConfigRepository
import com.aap.compose.weatherapp.repository.WeatherRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get :Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var weatherRepository: WeatherRepository
    @Inject
    lateinit var configRepository: ConfigRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun homeScreen_no_previousLocation_searchViewIsVisible() {
        val homeScreenViewModel = HomeScreenViewModel(weatherRepository, configRepository)
        composeTestRule.setContent {
            HomeScreen(onLocationClicked = { _, _, _, _, _ -> }, Modifier, homeScreenViewModel)
        }
        val contentDescSearch = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.search_icon_content_desc)
        val contentDescClose = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.search_close_icon_content_desc)
        composeTestRule.onNodeWithContentDescription(contentDescSearch).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(contentDescClose).assertIsDisplayed()

    }
}