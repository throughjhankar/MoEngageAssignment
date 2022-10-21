package com.sachinverma.moengageassignment.network

import com.sachinverma.moengageassignment.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Sachin Verma on 2020-01-03.
 */

object APIClient {

    private var retrofit: Retrofit? = null

    init {
        val interceptor = RetrofitInterceptor()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.apiBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getClient() = retrofit

}