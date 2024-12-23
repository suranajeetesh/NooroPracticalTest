@file:Suppress("BlockingMethodInNonBlockingContext")

package com.nooro.weathertracker.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nooro.weathertracker.data.remote.model.response.weatherData.Error
import com.nooro.weathertracker.network.HttpConstants.SOMETHING_WANT_WRONG
import org.json.JSONObject
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Created by Jeetesh Surana.
 * Use for give the error meaningful name for norm user
 */

abstract class SafeApiRequest(val context: Context) {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            val message = StringBuilder()
            var errNo: String? = null
            var code: String? = null

            if (!errorBody.isNullOrBlank()) {
                try {
                    val errorData = JSONObject(errorBody).getJSONObject("error").optString("message")
                    // Append the server message if present
                    if (!errorData.isNullOrEmpty()) {
                        message.append(errorData)
                    } else {
                        message.append(SOMETHING_WANT_WRONG)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    message.append(SOMETHING_WANT_WRONG)
                }
            } else {
                if (response.code() == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                    code = HttpURLConnection.HTTP_CLIENT_TIMEOUT.toString()
                    message.append(SOMETHING_WANT_WRONG)
                    errNo = HttpURLConnection.HTTP_CLIENT_TIMEOUT.toString()
                } else {
                    message.append("Unknown error occurred")
                }
            }

            throw ApiException(
                message = message.toString(),
                errno = errNo,
                code = response.code()
            )
        }
    }
    private fun getErrorClassObject (json: String): Error {
    	val type = object : TypeToken<Error>() {}.type
    	return Gson().fromJson(json, type)
    }
}
