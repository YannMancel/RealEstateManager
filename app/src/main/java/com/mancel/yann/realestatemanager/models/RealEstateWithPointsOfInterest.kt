package com.mancel.yann.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Created by Yann MANCEL on 03/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 *
 * Many-to-many relationships: many [RealEstate] and many [PointOfInterest]
 */
data class RealEstateWithPointsOfInterest(

    @Embedded
    var mRealEstate: RealEstate? = null,

    @Relation(parentColumn = "id_real_estate",
              entityColumn = "id_point_of_interest",
              associateBy = Junction(RealEstatePointOfInterestCrossRef::class))
    var mPointsOfInterest: List<PointOfInterest>? = null
)