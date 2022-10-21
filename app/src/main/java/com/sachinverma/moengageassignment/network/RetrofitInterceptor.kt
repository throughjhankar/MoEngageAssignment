package com.sachinverma.moengageassignment.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Sachin Verma on 2020-01-06.
 */

class RetrofitInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}