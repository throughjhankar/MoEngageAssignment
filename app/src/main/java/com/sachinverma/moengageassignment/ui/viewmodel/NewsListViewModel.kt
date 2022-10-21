package com.sachinverma.moengageassignment.ui.viewmodel

import android.content.Context
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sachinverma.moengageassignment.model.News
import com.sachinverma.moengageassignment.model.Response
import com.sachinverma.moengageassignment.network.APIClient
import com.sachinverma.moengageassignment.network.APIInterface
import com.sachinverma.moengageassignment.utils.Constants
import com.sachinverma.moengageassignment.utils.Logger
import com.sachinverma.moengageassignment.utils.UtilityFunctions
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by Sachin Verma on 2020-01-03.
 */

class NewsListViewModel(private val context: Context) : ViewModel() {

    var apiInterface: APIInterface? = null

    private var _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>>
        get() = _newsList

    private val callback = object : Callback<Response> {

        override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
            Logger.i("network response : ${response.body()}")
            val temp = response.body() as Response
            _newsList.value = temp.articles
        }

        override fun onFailure(call: Call<Response>, t: Throwable) {
            Logger.i("network onFailure : ${t.printStackTrace()}")
        }
    }

    init {
        if (UtilityFunctions.isNetworkAvailable(context))
            loadData()
        else
            Toast.makeText(context, "No internet connectivity", Toast.LENGTH_SHORT).show()
    }

    private fun loadData () {
        apiInterface = APIClient.getClient()!!.create(APIInterface::class.java)

        val params = HashMap<String, String>()

//        params["country"] = "in"

        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val countryCode = manager.simCountryIso

        Logger.d("countryCode: " + ConfigurationCompat.getLocales(context.resources.configuration).get(0).country)

        params["country"] = if (countryCode == null || countryCode.isEmpty()) ConfigurationCompat.getLocales(context.resources.configuration).get(0).country else countryCode

        params["apiKey"] = Constants.apiKey

        val call: Call<Response> = apiInterface!!.doGetNewsList(params)

        call.enqueue(callback)
    }



}