package com.aap.compose.weatherapp


import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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

const val REQUEST_CODE = 1001

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var locationPermissionGranted = false
    val requestPermissionLauncher =
        this.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

            } else {

            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
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
}

