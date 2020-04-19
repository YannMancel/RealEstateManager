package com.mancel.yann.realestatemanager.utils

import android.content.Context
import androidx.core.content.edit

/**
 * Created by Yann MANCEL on 18/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.utils
 */
object SaveTools {

    // FIELDS --------------------------------------------------------------------------------------

    private const val SAVE_FILE_NAME = "com.mancel.yann.realestatemanager.SAVE_FILE_NAME"

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Saves a [Boolean] thanks to SharedPreferences
     * @param context   a [Context]
     * @param key       a [String] that contains the key
     * @param value     a [Boolean] that contains the value
     */
    fun saveBooleanIntoSharedPreferences(
        context: Context,
        key: String,
        value: Boolean
    ) {
        context.getSharedPreferences(SAVE_FILE_NAME, Context.MODE_PRIVATE)
               .edit {
                   putBoolean(key, value)
               }
    }

    /**
     * Fetches a [Boolean] from SharedPreferences
     * @param context   a [Context]
     * @param key       a [String] that contains the key
     * @return a [Boolean] that contains the value
     */
    fun fetchBooleanFromSharedPreferences(
        context: Context,
        key: String
    ): Boolean {
        return context.getSharedPreferences(SAVE_FILE_NAME, Context.MODE_PRIVATE)
                      .getBoolean(key, true)
    }
}