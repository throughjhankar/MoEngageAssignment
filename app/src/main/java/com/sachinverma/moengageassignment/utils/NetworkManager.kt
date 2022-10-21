package com.sachinverma.moengageassignment.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import org.koin.java.KoinJavaComponent
import timber.log.Timber

/**
 * This class is responsible for notifying network state, whether it is available or not to the
 * registered class.
 */
object NetworkManager {

    private val context: Context by KoinJavaComponent.inject(Context::class.java)
    private var networkStateListener: NetworkStateListener? = null

    interface NetworkStateListener {
        fun onAvailable()
        fun onLost()
    }

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Timber.tag(TAG).i("Network Available")
            networkStateListener?.onAvailable()
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            Timber.tag(TAG).i("Network Lost")
            networkStateListener?.onLost()
        }
    }

    fun registerNetworkStateListener(networkStateListener: NetworkStateListener) {
        this.networkStateListener = networkStateListener
    }

    /**
     * Checks for network state on demand
     */
    fun checkConnection() {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}

private const val TAG = "NetworkManager"
