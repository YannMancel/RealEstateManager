package com.mancel.yann.realestatemanager.models

import androidx.room.*
import java.util.*

/**
 * Created by Yann MANCEL on 01/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 *
 * It is the real estate.
 *
 * One-to-many relationships: one [User] and many [RealEstate]
 * One-to-many relationships: one [RealEstate] and many [Photo]
 * Many-to-many relationships: many [RealEstate] and many [PointOfInterest]
 */
@Entity(tableName = "real_estate",
        foreignKeys = [ForeignKey(entity = User::class,
                                  parentColumns = ["user_id"],
                                  childColumns = ["estate_agent_id"],
                                  onDelete = ForeignKey.NO_ACTION,
                                  onUpdate = ForeignKey.CASCADE)])
data class RealEstate(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "real_estate_id")
    var mId: Long = 0L,

    @ColumnInfo(name = "type")
    var mType: String? = null,

    @ColumnInfo(name = "price_dollar")
    var mPrice: Double? = null,

    @ColumnInfo(name = "surface_m2")
    var mSurface: Double? = null,

    @ColumnInfo(name = "number_of_room")
    var mNumberOfRoom: Int? = null,

    @ColumnInfo(name = "description")
    var mDescription: String? = null,

    @ColumnInfo(name = "is_enable")
    var mIsEnable: Boolean? = null,

    @ColumnInfo(name = "effective_date")
    var mEffectiveDate: Date? = null,

    @ColumnInfo(name = "sale_date")
    var mSaleDate: Date? = null,

    @ColumnInfo(name = "estate_agent_id")
    var mEstateAgentId: Long? = null,

    @Embedded(prefix = "loc_")
    var mAddress: Address? = null
)