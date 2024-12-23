package com.nooro.weathertracker.network.interceptor

import com.nooro.weathertracker.network.HttpConstants.HEADER_PARAMETER
import com.nooro.weathertracker.network.HttpConstants.WEATHER_API_KEY
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT

/**
 * Created by Jeetesh Surana.
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        return try {
            chain.run {
                val builder: Request.Builder = request().newBuilder()
                builder.addHeader(HEADER_PARAMETER,WEATHER_API_KEY)
                proceed(builder.build())
            }
        } catch (e: Exception) {
            Response.Builder()
                .code(HTTP_CLIENT_TIMEOUT)
                .request(chain.request())
                .body(object : ResponseBody() {
                    override fun contentLength() = 0L
                    override fun contentType(): MediaType? = null
                    override fun source(): BufferedSource = Buffer()
                })
                .message(e.message ?: e.toString())
                .protocol(Protocol.HTTP_1_1)
                .build()
        }
    }
}
