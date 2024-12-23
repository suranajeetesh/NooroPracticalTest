package com.nooro.weathertracker.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nooro.weathertracker.data.remote.model.response.weatherData.WeatherDataResponse

/**
 * Created by Jeetesh Surana.
 */
class PreferenceProvider(context: Context) {
    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    private val gson = Gson()

    // Save ArrayList<WeatherDataResponse>
    fun putWeatherDataList(key: String, value: ArrayList<WeatherDataResponse>) {
        val json = gson.toJson(value)
        preference.edit()
            .putString(key, json)
            .apply()
    }

    // Retrieve ArrayList<WeatherDataResponse>
    fun getWeatherDataList(key: String): ArrayList<WeatherDataResponse> {
        val json = preference.getString(key, null)
        val type = object : TypeToken<ArrayList<WeatherDataResponse>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }
}
