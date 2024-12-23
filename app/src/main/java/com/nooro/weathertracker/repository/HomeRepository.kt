package com.nooro.weathertracker.repository

import android.content.Context
import com.nooro.weathertracker.data.remote.model.response.weatherData.WeatherDataResponse
import com.nooro.weathertracker.network.ApiRestService
import com.nooro.weathertracker.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class HomeRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val api: ApiRestService
) : SafeApiRequest(context) {

    suspend fun getUserData(query:String?): WeatherDataResponse {
        return apiRequest { api.getWeatherData(query) }
    }


}