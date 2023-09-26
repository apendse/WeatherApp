package com.aap.compose.weatherapp.location

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LocationModule {
    @Singleton
    @Binds
    fun bindLocationProvider(locationProviderImpl: LocationProviderImpl): LocationProvider
}