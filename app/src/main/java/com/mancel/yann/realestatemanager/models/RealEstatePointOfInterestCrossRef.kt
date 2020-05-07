package com.mancel.yann.realestatemanager.models

import androidx.room.*

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
        primaryKeys = ["id_real_estate", "id_point_of_interest"],
        foreignKeys = [ForeignKey(entity = RealEstate::class,
                                  parentColumns = ["id_real_estate"],
                                  childColumns = ["id_real_estate"],
                                  onDelete = ForeignKey.CASCADE,
                                  onUpdate = ForeignKey.CASCADE),
                       ForeignKey(entity = PointOfInterest::class,
                                  parentColumns = ["id_point_of_interest"],
                                  childColumns = ["id_point_of_interest"],
                                  onDelete = ForeignKey.CASCADE,
                                  onUpdate = ForeignKey.CASCADE)],
        indices = [Index(value = ["id_real_estate"]),
                   Index(value = ["id_point_of_interest"])])
data class RealEstatePointOfInterestCrossRef(

    @ColumnInfo(name = "id_real_estate")
    var mRealEstateId: Long = 0L,

    @ColumnInfo(name = "id_point_of_interest")
    var mPointOfInterestId: Long = 0L
)