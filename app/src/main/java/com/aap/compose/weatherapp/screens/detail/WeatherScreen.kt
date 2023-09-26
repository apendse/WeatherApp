package com.aap.compose.weatherapp.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aap.compose.weatherapp.R
import com.aap.compose.weatherapp.data.Coordinates
import com.aap.compose.weatherapp.data.TemperatureData
import com.aap.compose.weatherapp.data.WeatherData
import com.aap.compose.weatherapp.data.WeatherItem
import com.aap.compose.weatherapp.repository.Units

/**
 * Composable that renders the actual weather data
 */
@Composable
fun WeatherScreen(modifier: Modifier = Modifier, weatherData: WeatherData, units: Units) {
    val temparatureSuffix = if (units == Units.IMPERIAL) {
        stringResource(id = R.string.imperial_temperature_suffix)
    } else {
        stringResource(id = R.string.metric_temperature_suffix)
    }

    Column(
        modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Location(weatherData)
        TemperatureRow(weatherData, temparatureSuffix)
        FeelsRow(weatherData, temparatureSuffix)
        DescriptionRow(weatherData)
        HighLowTemperature(weatherData.temperatures, temparatureSuffix)
        HumidityRow(weatherData)
        Icon(weatherData)
    }
}

@Composable
private fun HumidityRow(weatherData: WeatherData) {

    Row(modifier = Modifier) {
        //Text(text = "Humidity: ${weatherData.temperatures.humidity} %", fontSize = 20.sp)
        Text(text = stringResource(id = R.string.humidity_template, weatherData.temperatures.humidity), fontSize = 20.sp)
    }

}


@Composable
private fun Location(weatherData: WeatherData) {
    Row(modifier = Modifier) {
        Text(text = weatherData.name, fontSize = 20.sp)
    }
}

@Composable
private fun TemperatureRow(weatherData: WeatherData, suffix: String) {
    Row(modifier = Modifier) {
        val temperatureData = weatherData.temperatures
        Text(text = "current: ${temperatureData.temp}$suffix", fontSize = 20.sp)
    }

}


@Composable
private fun FeelsRow(weatherData: WeatherData, suffix: String) {
    Row(modifier = Modifier) {
        val temperatureData = weatherData.temperatures
        Text(text = "Feels like ${temperatureData.feelsLike}$suffix", fontSize = 18.sp)
    }

}

@Composable
private fun DescriptionRow(weatherData: WeatherData) {
    if (weatherData.weather.isEmpty()) {
        return
    }
    val weather = weatherData.weather[0]
    Row(modifier = Modifier) {
        Text(weather.description)

    }
}

@Composable
private fun Icon(weatherData: WeatherData) {
    if (weatherData.weather.isEmpty()) {
        return
    }
    val weather = weatherData.weather[0]
    AsyncImage(
        modifier = Modifier
            .width(80.dp)
            .width(80.dp),
        model = getImagePath(weather.icon),
        contentDescription = stringResource(id = R.string.image_description)
    )
}

@Composable
private fun HighLowTemperature(temperatureData: TemperatureData, suffix: String) {
    Row(modifier = Modifier) {
        Text(stringResource(id = R.string.max_temp_label))
        Text("${temperatureData.tempMax}$suffix", color = Color(0xFFdd0066))
    }
    Row(modifier = Modifier) {
        Text(stringResource(id = R.string.min_temp_label))
        Text("${temperatureData.tempMin}$suffix", color = Color(0xFF1A6B46))
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    val weatherData = WeatherData(
        Coordinates(0.0, 0.0),
        listOf(WeatherItem(0, "main", "Mostly Sunny", "50d")),
        TemperatureData(
            temp = 80.1,
            feelsLike = 80.1,
            tempMin = 55.0,
            tempMax = 82.0,
            humidity = 56.789
        ),
        "Milpitas"
    )
    WeatherScreen(Modifier, weatherData, Units.IMPERIAL)
}

private fun getImagePath(image: String) = "https://openweathermap.org/img/wn/$image@2x.png"