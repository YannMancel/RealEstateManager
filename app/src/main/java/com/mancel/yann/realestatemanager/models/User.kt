package com.mancel.yann.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Yann MANCEL on 26/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.models
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val mId: Long,

    @ColumnInfo(name = "username")
    val mUsername: String?,

    @ColumnInfo(name = "email")
    val mEmail: String?,

    @ColumnInfo(name = "url_picture")
    val mUrlPicture: String?
)