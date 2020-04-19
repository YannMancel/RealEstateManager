package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.dao.PhotoDAO
import com.mancel.yann.realestatemanager.models.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Yann MANCEL on 05/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 *
 * A class which implements [PhotoRepository].
 */
class PhotoRepositoryImpl(
    private val mPhotoDAO: PhotoDAO
) : PhotoRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    override suspend fun insertPhotos(
        vararg photos: Photo
    ): List<Long> = withContext(Dispatchers.IO) {
        this@PhotoRepositoryImpl.mPhotoDAO.insertPhotos(*photos)
    }

    // -- Read --

    override fun getAllPhotos(): LiveData<List<Photo>> = this.mPhotoDAO.getAllPhotos()

    // -- Update --

    override suspend fun updatePhoto(photo: Photo): Int = withContext(Dispatchers.IO) {
        this@PhotoRepositoryImpl.mPhotoDAO.updatePhoto(photo)
    }

    // -- Delete --

    override suspend fun deletePhoto(photo: Photo): Int = withContext(Dispatchers.IO) {
        this@PhotoRepositoryImpl.mPhotoDAO.deletePhoto(photo)
    }
}