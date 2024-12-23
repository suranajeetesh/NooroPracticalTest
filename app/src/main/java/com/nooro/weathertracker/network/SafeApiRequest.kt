@file:Suppress("BlockingMethodInNonBlockingContext")

package com.nooro.weathertracker.network

import android.content.Context
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
                    message.append(SOMETHING_WANT_WRONG)
                    errNo = HttpURLConnection.HTTP_CLIENT_TIMEOUT.toString()
                } else {
                    message.append("Unknown error occurred")
                }
            }

            throw ApiException(
                message = message.toString(),
                code = response.code()
            )
        }
    }
}
