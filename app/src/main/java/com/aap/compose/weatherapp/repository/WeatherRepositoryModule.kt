package com.aap.compose.weatherapp.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface WeatherRepositoryModule  {
    @Binds
    fun bindWeatherRepository(weatherRepository: WeatherRepositoryImpl) : WeatherRepository
}