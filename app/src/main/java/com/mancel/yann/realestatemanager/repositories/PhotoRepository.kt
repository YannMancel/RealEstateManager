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

    suspend fun insertPhotos(vararg photos: Photo): List<Long>

    // -- Read --

    fun getAllPhotos(): LiveData<List<Photo>>

    // -- Update --

    suspend fun updatePhoto(photo: Photo): Int

    // -- Delete --

    suspend fun deletePhoto(photo: Photo): Int
}