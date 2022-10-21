package com.sachinverma.moengageassignment.utils

import android.content.Context
import android.net.ConnectivityManager


/**
 * Created by Sachin Verma on 2020-01-06.
 */

object UtilityFunctions {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}