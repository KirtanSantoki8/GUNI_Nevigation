package com.devkt.guninevigation.di

import com.devkt.guninevigation.api.ApiBuilder
import com.devkt.guninevigation.repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    @Provides
    @Singleton
    fun provideApi(): ApiBuilder{
        return ApiBuilder
    }

    @Provides
    @Singleton
    fun provideRepo(api: ApiBuilder): Repo{
        return Repo(api)
    }
}