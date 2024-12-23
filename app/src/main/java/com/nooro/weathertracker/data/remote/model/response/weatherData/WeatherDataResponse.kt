package com.nooro.weathertracker.data.remote.model.response.weatherData

data class WeatherDataResponse(
    var current: Current? = null,
    var location: Location? = null,
    var error: Error? = null
)