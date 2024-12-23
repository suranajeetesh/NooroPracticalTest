package com.nooro.weathertracker.data.remote.model.response.weatherData

data class Condition(
    var code: Int? = null,
    var icon: String? = null,
    var text: String? = null
)