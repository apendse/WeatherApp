package com.aap.compose.weatherapp.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aap.compose.weatherapp.data.LocationParam
import com.aap.compose.weatherapp.screens.detail.WeatherDetailScreen
import com.aap.compose.weatherapp.screens.home.HomeScreen

@Composable
fun NavigationGraph(modifier: Modifier, navController: NavHostController) {
    NavHost(modifier = modifier, navController = navController, startDestination = NavDestinations.HOME) {
        composable(NavDestinations.HOME) {
            HomeScreen({ latitude, longitude ->
                navController.navigate("${NavDestinations.WEATHER_DETAILS}/${latitude}/${longitude}")
            })
        }

        composable(
            NavDestinations.WEATHER_DETAIL_WITH_PARAM_PLACE_HOLDERS,
            arguments = listOf(
                navArgument(NavDestinations.LATITUDE_PARAM) { type = NavType.FloatType },
                navArgument(NavDestinations.LONGITUDE_PARAM) { type = NavType.FloatType },
            )
        ) { backStackEntry ->
            WeatherDetailScreen(
                modifier,
                LocationParam(
                    backStackEntry.arguments?.getFloat(NavDestinations.LATITUDE_PARAM)?.toDouble()
                        ?: 0.0,
                    backStackEntry.arguments?.getFloat(NavDestinations.LONGITUDE_PARAM)?.toDouble()
                        ?: 0.0
                ),
            )
        }
    }
}

private fun getTrimmedString(str: String?): String {
    val str1 = str ?: return "0.0"
    return str1.substring(1)
}