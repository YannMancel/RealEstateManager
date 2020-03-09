package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.models.Photo

/**
 * Created by Yann MANCEL on 06/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 */
interface PhotoRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    fun insertPhoto(photo: Photo): Long

    fun insertPhotos(vararg photos: Photo): List<Long>

    // -- Read --

    fun getPhotoByRealEstateId(realEstateId: Long): LiveData<List<Photo>>

    fun getAllPhotos(): LiveData<List<Photo>>

    // -- Update --

    fun updatePhoto(photo: Photo): Int

    // -- Delete --

    fun deletePhoto(photo: Photo): Int
}