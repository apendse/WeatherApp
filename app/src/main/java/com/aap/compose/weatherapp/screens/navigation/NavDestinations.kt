package com.aap.compose.weatherapp.screens.navigation

object NavDestinations {
    const val HOME = "home"
    const val LATITUDE_PARAM = "latitude"
    const val LONGITUDE_PARAM = "longitude"
    const val WEATHER_DETAILS = "weatherDetails"
    const val WEATHER_DETAIL_WITH_PARAM_PLACE_HOLDERS = "${WEATHER_DETAILS}/{${LATITUDE_PARAM}}/{${LONGITUDE_PARAM}}"
}