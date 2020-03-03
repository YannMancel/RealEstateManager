package com.mancel.yann.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Embedded

/**
 * Created by Yann MANCEL on 02/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 *
 * It is a class to retrieve specific fields of [RealEstate].
 */
data class IdTypeAddressPriceTupleOfRealEstate(

    @ColumnInfo(name = "id")
    var mId: Long = 0L,

    @ColumnInfo(name = "type")
    var mType: String? = null,

    @Embedded(prefix = "loc_")
    var mAddress: Address? = null,

    @ColumnInfo(name = "price_dollar")
    var mPrice: Double? = null
)