package com.mancel.yann.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Yann MANCEL on 26/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 *
 * It is the estate agent.
 *
 * One-to-many relationships: one [User] and many [RealEstate]
 */
@Entity(tableName = "user",
        indices = [Index(value = ["username", "email"],
                         unique = true)])
data class User(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var mId: Long = 0L,

    @ColumnInfo(name = "username")
    var mUsername: String? = null,

    @ColumnInfo(name = "email")
    var mEmail: String? = null,

    @ColumnInfo(name = "url_picture")
    var mUrlPicture: String? = null
)