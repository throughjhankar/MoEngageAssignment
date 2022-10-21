package com.sachinverma.moengageassignment.network

import com.sachinverma.moengageassignment.utils.Constants
import com.sachinverma.moengageassignment.model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by Sachin Verma on 2020-01-03.
 */
interface APIInterface {

    @GET(Constants.apiNewsList)
    fun doGetNewsList(@QueryMap params: Map<String, String>): Call<Response>

}