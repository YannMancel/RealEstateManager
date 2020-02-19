package com.mancel.yann.realestatemanager.utils

import android.content.Context
import android.net.wifi.WifiManager
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
    fun convertDollarToEuro(dollars: Int): Int = (dollars.toFloat() * 0.812).roundToInt()

    /**
     * Converts the price of real estate (Euros to Dollars)
     * @param euros an [Int] that contains the value in Euros
     * @return an [Int] that contains the value in Dollars
     */
    fun convertEuroToDollar(euros: Int): Int = (euros.toFloat() / 0.812).roundToInt()

    // -- Date --

    /**
     * Converts the current date in yyyy/MM/dd format
     * @return a [String] that contains the format
     */
    fun getTodayDateYYYYMMDD(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        return dateFormat.format(Date())
    }

    /**
     * Converts the current date in dd/MM/yyyy format
     * @return a [String] that contains the format
     */
    fun getTodayDateDDMMYYYY(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(Date())
    }

    // -- Network --

    /**
     * Checks the network connection
     * @param context a [Context]
     * @return a [Boolean] that returns true is there is a network connection
     */
    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }