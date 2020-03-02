package com.mancel.yann.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Created by Yann MANCEL on 01/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 *
 * It is the photo of real estate.
 *
 * One-to-many relationships: one [RealEstate] and many [Photo]
 */
@Entity(tableName = "photo",
        foreignKeys = [ForeignKey(entity = RealEstate::class,
                                  parentColumns = ["real_estate_id"],
                                  childColumns = ["estate_id"],
                                  onDelete = ForeignKey.CASCADE,
                                  onUpdate = ForeignKey.CASCADE)])
data class Photo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "photo_id")
    var mId: Long = 0L,

    @ColumnInfo(name = "url_picture")
    val mUrlPicture: String? = null,

    @ColumnInfo(name = "description")
    var mDescription: String? = null,

    @ColumnInfo(name = "estate_id")
    var mRealEstateId: Long? = null
)