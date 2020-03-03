package com.mancel.yann.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Yann MANCEL on 02/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 *
 * One-to-many relationships: one [RealEstate] and many [Photo]
 */
data class RealEstateWithPhotos(

    @Embedded
    var mRealEstate: RealEstate? = null,

    @Relation(parentColumn = "id_real_estate",
              entityColumn = "real_estate_id")
    var mPhotos: List<Photo>? = null
)