package com.aap.compose.weatherapp.network

import com.aap.compose.weatherapp.data.GeoLocationData
import com.aap.compose.weatherapp.data.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query


//https://api.openweathermap.org/data/2.5/weather?q=San Francisco,usa&APPID=ea532800f653719e0a32716ac930d92c&units=imperial
// ea532800f653719e0a32716ac930d92c

//

// http://api.openweathermap.org/geo/1.0/zip?zip=95035,usa&appid=ea532800f653719e0a32716ac930d92c

// http://api.openweathermap.org/geo/1.0/direct?q=princeton&limit=5&appid=a532800f653719e0a32716ac930d92c
/// http://api.openweathermap.org/geo/1.0/direct?q={city%20name},{state%20code},{country%20code}&limit={limit}&appid={API%20key}
// geo coding

//https://api.openweathermap.org/geo/1.0/zip?zip=95035,us&appid=ea532800f653719e0a32716ac930d92c
// https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
// https://api.openweathermap.org/data/2.5/weather?lat=33.18011474609375&long=-96.49803924560547&appid=ea532800f653719e0a32716ac930d92c
    // https://api.openweathermap.org/data/2.5/weather?lat=21.48343645&lon=-158.03648372697478&appid=ea532800f653719e0a32716ac930d92c
interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getWeatherForGeoLocation(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): WeatherData

    @GET("geo/1.0/direct")
    suspend fun getLatLongForLocation(
        @Query("q") location: String,
        @Query("appid") appId: String,
        @Query("limit") limit: Int
    ): List<GeoLocationData>

    @GET("geo/1.0/zip")
    suspend fun getLatLongForZip(
        @Query("zip") zip: String,
        @Query("appid") appId: String
    ): GeoLocationData
}