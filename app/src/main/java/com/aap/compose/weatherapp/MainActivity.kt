package com.aap.compose.weatherapp


import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.aap.compose.weatherapp.screens.navigation.NavigationGraph
import com.aap.compose.weatherapp.ui.theme.Green40
import com.aap.compose.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var locationCallbackInvoked = false
    private var locationPermissionGranted = false
    private val requestPermissionLauncher =
        this.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            locationCallbackInvoked = true
            locationPermissionGranted = isGranted
            if (locationPermissionGranted) {
                viewModel.handleLocationPermissionGranted()
            }
        }

    val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!locationCallbackInvoked) {
            checkLocationPermission()
        }
        setContent {
            val navController = rememberNavController()
            WeatherAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.background(Green40),
                            title = { Text(text = stringResource(id = R.string.app_name)) })
                    }
                ) {
                    NavigationGraph(Modifier.padding(it), navController = navController)
                }
            }
        }


    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            viewModel.handleLocationPermissionGranted()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }

    }
}
