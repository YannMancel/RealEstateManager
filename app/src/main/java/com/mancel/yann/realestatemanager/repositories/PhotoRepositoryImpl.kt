package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.dao.PhotoDAO
import com.mancel.yann.realestatemanager.models.Photo

/**
 * Created by Yann MANCEL on 05/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 *
 * A class which implements [PhotoRepository].
 */
class PhotoRepositoryImpl(private val mPhotoDAO: PhotoDAO) : PhotoRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    override fun insertPhoto(photo: Photo): Long {
        TODO("Not yet implemented")
    }

    override fun insertPhotos(vararg photos: Photo): List<Long> {
        TODO("Not yet implemented")
    }

    // -- Read --

    override fun getPhotoByRealEstateId(realEstateId: Long): LiveData<List<Photo>> {
        TODO("Not yet implemented")
    }

    override fun getAllPhotos(): LiveData<List<Photo>> {
        TODO("Not yet implemented")
    }

    // -- Update --

    override fun updatePhoto(photo: Photo): Int {
        TODO("Not yet implemented")
    }

    // -- Delete --

    override fun deletePhoto(photo: Photo): Int {
        TODO("Not yet implemented")
    }
}