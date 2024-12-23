package com.nooro.weathertracker.di

import android.content.Context
import com.nooro.weathertracker.network.ApiRestService
import com.nooro.weathertracker.network.interceptor.HeaderInterceptor
import com.nooro.weathertracker.network.interceptor.NetworkInterceptor
import com.nooro.weathertracker.util.PreferenceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Jeetesh Surana.
 */

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun getNetworkInterceptor(@ApplicationContext appContext: Context) =
        NetworkInterceptor(appContext)

    @Provides
    @Singleton
    fun getHeaderInterceptor(preferenceProvider: PreferenceProvider) =
        HeaderInterceptor(preferenceProvider)

    @Provides
    @Singleton
    fun getApiRestService(
        @ApplicationContext appContext: Context,
        networkInterceptor: NetworkInterceptor,
        headerInterceptor: HeaderInterceptor
    ) = ApiRestService(appContext, networkInterceptor, headerInterceptor)

}