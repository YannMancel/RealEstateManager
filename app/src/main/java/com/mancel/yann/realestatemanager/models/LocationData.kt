package com.mancel.yann.realestatemanager.models

import android.location.Location

/**
 * Created by Yann MANCEL on 07/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 */
data class LocationData(
    val mLocation: Location? = null,
    val mException: Exception? = null
)