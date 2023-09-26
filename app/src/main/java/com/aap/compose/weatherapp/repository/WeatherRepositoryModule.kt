package com.aap.compose.weatherapp.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WeatherRepositoryModule {
    @Singleton
    @Binds
    fun bindWeatherRepository(weatherRepository: WeatherRepositoryImpl): WeatherRepository
}