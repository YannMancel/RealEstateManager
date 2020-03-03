package com.mancel.yann.realestatemanager.models

import androidx.room.*

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
                                  parentColumns = ["id_real_estate"],
                                  childColumns = ["real_estate_id"],
                                  onDelete = ForeignKey.CASCADE,
                                  onUpdate = ForeignKey.CASCADE)],
        indices = [Index(value = ["url_picture"],
                         unique = true),
                   Index(value = ["real_estate_id"])])
data class Photo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_photo")
    var mId: Long = 0L,

    @ColumnInfo(name = "url_picture")
    val mUrlPicture: String? = null,

    @ColumnInfo(name = "description")
    var mDescription: String? = null,

    @ColumnInfo(name = "real_estate_id")
    var mRealEstateId: Long? = null
)