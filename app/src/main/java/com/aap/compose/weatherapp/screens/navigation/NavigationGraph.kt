package com.aap.compose.weatherapp.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.screens.detail.WeatherDetailScreen
import com.aap.compose.weatherapp.screens.home.HomeScreen

@Composable
fun NavigationGraph(modifier: Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavDestinations.HOME
    ) {
        composable(NavDestinations.HOME) {
            HomeScreen({ latitude, longitude, name, country, state, isCurrentLocation ->
                navController.navigate("${NavDestinations.WEATHER_DETAILS}/${latitude}/${longitude}/${name}/${country}/${state}/${isCurrentLocation}")
            })
        }

        composable(
            NavDestinations.WEATHER_DETAIL_WITH_PARAM_PLACE_HOLDERS,
            arguments = listOf(
                navArgument(NavDestinations.LATITUDE_PARAM) { type = NavType.FloatType },
                navArgument(NavDestinations.LONGITUDE_PARAM) { type = NavType.FloatType },
                navArgument(NavDestinations.LOCATION_NAME_PARAM) { type = NavType.StringType },
                navArgument(NavDestinations.LOCATION_COUNTRY_PARAM) { type = NavType.StringType },
                navArgument(NavDestinations.LOCATION_STATE_PARAM) { type = NavType.StringType },
                navArgument(NavDestinations.IS_CURRENT_LOCATION) { type = NavType.BoolType },
            )
        ) { backStackEntry ->
            WeatherDetailScreen(
                modifier,
                GeoLocationData(
                    backStackEntry.arguments?.getString(NavDestinations.LOCATION_NAME_PARAM, "")
                        ?: "",

                    backStackEntry.arguments?.getFloat(NavDestinations.LATITUDE_PARAM)?.toDouble()
                        ?: 0.0,
                    backStackEntry.arguments?.getFloat(NavDestinations.LONGITUDE_PARAM)?.toDouble()
                        ?: 0.0,
                    backStackEntry.arguments?.getString(NavDestinations.LOCATION_COUNTRY_PARAM, "")
                        ?: "",
                    backStackEntry.arguments?.getString(NavDestinations.LOCATION_COUNTRY_PARAM, "")
                        ?: "",
                    backStackEntry.arguments?.getBoolean(NavDestinations.IS_CURRENT_LOCATION, false) ?: false
                ),
            )
        }
    }
}

