package com.mancel.yann.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Created by Yann MANCEL on 02/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 *
 * It is cross-reference table.
 *
 * Many-to-many relationships: many [RealEstate] and many [PointOfInterest]
 */
@Entity(tableName = "real_estate_point_of_interest_join",
        primaryKeys = ["real_estate_id", "point_of_interest_id"],
        foreignKeys = [ForeignKey(entity = RealEstate::class,
                                  parentColumns = ["id"],
                                  childColumns = ["real_estate_id"],
                                  onDelete = ForeignKey.CASCADE,
                                  onUpdate = ForeignKey.CASCADE),
                       ForeignKey(entity = PointOfInterest::class,
                                  parentColumns = ["id"],
                                  childColumns = ["point_of_interest_id"],
                                  onDelete = ForeignKey.CASCADE,
                                  onUpdate = ForeignKey.CASCADE)])
data class RealEstatePointOfInterestCrossRef(

    @ColumnInfo(name = "real_estate_id")
    val mRealEstateId: Long,

    @ColumnInfo(name = "point_of_interest_id")
    val mPointOfInterestId: Long
)