package com.nooro.weathertracker.viewmodel

import android.app.Application
import android.util.Log
import com.nooro.weathertracker.core.uI.BaseViewModel
import com.nooro.weathertracker.data.remote.model.response.weatherData.Error
import com.nooro.weathertracker.data.remote.model.response.weatherData.WeatherDataResponse
import com.nooro.weathertracker.network.ApiException
import com.nooro.weathertracker.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by JeeteshSurana.
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    application: Application
) : BaseViewModel(application) {

     var weatherData = MutableStateFlow(WeatherDataResponse())
         private set

    var error = MutableStateFlow(Error())
        private set

    suspend fun getData(query:String) = withContext(Dispatchers.Main) {
        try {
            val data = repository.getUserData(query)
            weatherData.emit(data)
            Log.e("TAG", "getData() data--> $data")
        } catch (e: ApiException) {
            error.emit(Error(message = e.message))
            e.printStackTrace()
        }
    }

    fun updateWeatherList(weatherDataResponse: WeatherDataResponse){
        if (!listWeatherData.value.contains(weatherDataResponse)) {
            listWeatherData.update {
                val mutableList = it.toMutableList()
                mutableList.add(weatherDataResponse)
                mutableList as ArrayList<WeatherDataResponse>
            }
        }
    }
}