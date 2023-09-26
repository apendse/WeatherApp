package com.aap.compose.weatherapp.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Inject
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ConfigRepositoryModule::class]
)
interface TestConfigRepositoryModule {
    @Singleton
    @Binds
    fun bindConfigRepository(testConfigRepository: TestConfigRepository): ConfigRepository
}

class TestConfigRepository @Inject constructor(): ConfigRepository {
    override fun getUnits(): Units {
        return Units.IMPERIAL
    }

    override fun setUnits(units: Units) {
        // NOOP
    }

}