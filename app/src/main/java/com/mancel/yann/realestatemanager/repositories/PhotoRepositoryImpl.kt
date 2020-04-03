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

    override suspend fun insertPhoto(photo: Photo): Long = this.mPhotoDAO.insertPhoto(photo)

    override suspend fun insertPhotos(vararg photos: Photo): List<Long> {
        return this.mPhotoDAO.insertPhotos(*photos)
    }

    // -- Read --

    override fun getPhotoByRealEstateId(realEstateId: Long): LiveData<List<Photo>> {
        return this.mPhotoDAO.getPhotoByRealEstateId(realEstateId)
    }

    override fun getAllPhotos(): LiveData<List<Photo>> = this.mPhotoDAO.getAllPhotos()

    // -- Update --

    override suspend fun updatePhoto(photo: Photo): Int = this.mPhotoDAO.updatePhoto(photo)

    // -- Delete --

    override suspend fun deletePhoto(photo: Photo): Int = this.mPhotoDAO.deletePhoto(photo)
}