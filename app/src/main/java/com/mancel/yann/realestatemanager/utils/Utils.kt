package com.mancel.yann.realestatemanager.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.VisibleForTesting
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Yann MANCEL on 19/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.utils
 */

// -- Money --

/**
 * Converts the price of real estate (Dollars to Euros)
 * @param dollars an [Int] that contains the value in Dollars
 * @return an [Int] that contains the value in Euros
 */
@VisibleForTesting
fun convertDollarToEuro(dollars: Int): Int = (dollars.toFloat() * 0.812F).roundToInt()

/**
 * Converts the price of real estate (Euros to Dollars)
 * @param euros an [Int] that contains the value in Euros
 * @return an [Int] that contains the value in Dollars
 */
@VisibleForTesting
fun convertEuroToDollar(euros: Int): Int = (euros.toFloat() / 0.812F).roundToInt()

// -- Date --

/**
 * Converts the current date in yyyy/MM/dd format
 * @return a [String] that contains the format
 */
@VisibleForTesting
fun getTodayDateYYYYMMDD(): String {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return dateFormat.format(Date())
}

/**
 * Converts the current date in dd/MM/yyyy format
 * @return a [String] that contains the format
 */
@VisibleForTesting
fun getTodayDateDDMMYYYY(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date())
}

// -- Network --

/**
 * Checks the network connection
 * @param context a [Context]
 * @return a [Boolean] that returns true is there is a network connection
 */
@VisibleForTesting
fun isInternetAvailable(context: Context): Boolean {
    var isNetworkAvailable = false

    val connectivityManager = context.applicationContext
                                     .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    connectivityManager?.let {
        // API level >= API 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            it.getNetworkCapabilities(it.activeNetwork)?.let { capability ->
                if (capability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capability.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
                    || capability.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    || capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                    isNetworkAvailable = true
                }
            }
        }
        else {
            it.activeNetworkInfo?.let { networkInfo ->
                isNetworkAvailable = networkInfo.isConnected
            }
        }
    }

    return isNetworkAvailable
}