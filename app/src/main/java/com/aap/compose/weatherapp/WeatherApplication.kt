package com.aap.compose.weatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * An application class created so that it can be annotated with HiltAndroidApp so that Hilt can
 * generate the code with the correct scope.
 */
@HiltAndroidApp
class WeatherApplication : Application()
