package com.nooro.weathertracker.core.uI

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nooro.weathertracker.data.remote.model.response.weatherData.WeatherDataResponse
import com.nooro.weathertracker.util.Constant.SharedPreferenceKeys.WEATHER_LIST
import com.nooro.weathertracker.util.PreferenceProvider
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by JeeteshSurana.
 */

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenceProvider=PreferenceProvider(application)

    var listWeatherData = MutableStateFlow(ArrayList<WeatherDataResponse>())
        private set

    init {
        loadStoreData()
    }

    private fun loadStoreData() {
        val list=preferenceProvider.getWeatherDataList(WEATHER_LIST)
        if (list.isNotEmpty()){
            listWeatherData.value.addAll(list)
        }
    }

    private fun saveWeatherData() {
        preferenceProvider.putWeatherDataList(WEATHER_LIST,listWeatherData.value)
    }

    override fun onCleared() {
        saveWeatherData()
        super.onCleared()
    }

}