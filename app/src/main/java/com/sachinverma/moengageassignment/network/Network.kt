package com.sachinverma.moengageassignment.network

import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.SocketTimeoutException
import java.net.URL

object Network {
    private var resultListener: ResultListener? = null

    interface ResultListener {
        fun onResponse(response: String)
        fun onError(error: Exception)
    }

    fun setResultListener(resultListener: ResultListener) {
        this.resultListener = resultListener
    }

    fun sendRequest(url: String) {
        try {
            val urlNet = URL(url)
            with(urlNet.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                var line: String? = ""
                var result = ""
                inputStream.bufferedReader().use {
                    while (line != null) {
                        line = it.readLine()
                        if (line != null) {
                            result += line
                        }
                    }
                }
                resultListener!!.onResponse(result)
            }
        } catch (e: MalformedURLException) {
            resultListener!!.onError(e)
        } catch (e: SocketTimeoutException) {
            resultListener!!.onError(e)
        } catch (e: IOException) {
            resultListener!!.onError(e)
        }
    }

}

const val URL =
    "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
