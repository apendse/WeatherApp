package com.aap.compose.weatherapp.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ConfigRepositoryModule {
    @Binds
    fun bindConfigRepository(configRepository: ConfigRepositoryImpl): ConfigRepository
}