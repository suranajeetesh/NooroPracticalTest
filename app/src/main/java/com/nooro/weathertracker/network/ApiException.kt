package com.nooro.weathertracker.network

import java.io.IOException

/**
 * Created by Jeetesh Surana.
 */
class ApiException(message: String, var code: Int) : IOException(message)
