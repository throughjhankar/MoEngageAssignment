package com.sachinverma.moengageassignment.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sachinverma.moengageassignment.model.News
import com.sachinverma.moengageassignment.model.Response
import com.sachinverma.moengageassignment.network.Network
import com.sachinverma.moengageassignment.network.URL
import com.sachinverma.moengageassignment.utils.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber


class NewsListVM : ViewModel() {

    private var networkJob: Job? = null
    private val vmIoScope = viewModelScope + Dispatchers.IO
    private val _currentState: MutableLiveData<NewsListScreenState> =
        MutableLiveData(NewsListScreenState.Loading)
    val currentState: LiveData<NewsListScreenState> = _currentState

    private val callback = object : Network.ResultListener {
        override fun onResponse(response: String) {
            Timber.tag(TAG).i("Network onResponse : $response")
            val result = Gson().fromJson(response, Response::class.java)
            _currentState.postValue(NewsListScreenState.Data(result.articles))
        }

        override fun onError(error: Exception) {
            Timber.tag(TAG).i("Network onError : ${error.printStackTrace()}")
            _currentState.postValue(NewsListScreenState.Error("Some Error Occurred"))
        }
    }

    fun onViewCreated(networkStateListener: NetworkManager.NetworkStateListener) {
        checkNetworkState(networkStateListener)
    }

    fun onRetry(networkStateListener: NetworkManager.NetworkStateListener) {
        checkNetworkState(networkStateListener)
    }

    private fun checkNetworkState(networkStateListener: NetworkManager.NetworkStateListener) {
        NetworkManager.registerNetworkStateListener(networkStateListener)
        NetworkManager.checkConnection()
    }

    private fun fetchNewsFromNetwork() {
        if (networkJob != null) {
            networkJob?.cancel()
        }
        networkJob = vmIoScope.launch {
            Network.setResultListener(callback)
            Network.sendRequest(URL)
        }
    }

    fun onMenuItemTapped(sort: Sort, newsList: List<News>) {
        when (sort) {
            Sort.TITLE -> {
                vmIoScope.launch {
                    _currentState.postValue(NewsListScreenState.Data(newsList.sortedWith(compareBy { it.title })))
                }
            }
            Sort.DATE -> {
                vmIoScope.launch {
                    _currentState.postValue(NewsListScreenState.Data(newsList.sortedWith(compareBy { it.publishedAt })))
                }
            }
        }
    }

    fun onNewsItemTapped(context: Context, news: News) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(news.url)
        context.startActivity(browserIntent)
    }

    fun onNetworkAvailable() {
        _currentState.postValue(NewsListScreenState.Loading)
        fetchNewsFromNetwork()
    }

    fun onNetworkLost() {
        _currentState.postValue(NewsListScreenState.Error("No Internet Connection. Please connect to internet and try again."))
    }
}

enum class Sort {
    TITLE, DATE
}

sealed class NewsListScreenState {
    object Loading : NewsListScreenState()
    data class Data(val newsList: List<News>) : NewsListScreenState()
    data class Error(val errorMessage: String) : NewsListScreenState()
}

private const val TAG = "NewsListVM"
