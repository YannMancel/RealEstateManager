package com.mancel.yann.realestatemanager.models

import androidx.room.ColumnInfo

/**
 * Created by Yann MANCEL on 01/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 */
data class Address(

    @ColumnInfo(name = "street")
    val mStreet: String? = null,

    @ColumnInfo(name = "state")
    val mState: String? = null,

    @ColumnInfo(name = "city")
    val mCity: String? = null,

    @ColumnInfo(name = "post_code")
    val mPostCode: Int? = null
)