package com.mancel.yann.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mancel.yann.realestatemanager.models.Photo

/**
 * Created by Yann MANCEL on 03/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.dao
 *
 * DAO of [Photo].
 */
@Dao
interface PhotoDAO {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    /**
     * Usage:
     * val id = dao.insertPhoto(photo)
     */
    @Insert
    fun insertPhoto(photo: Photo): Long

    /**
     * Usage:
     * val ids = dao.insertPhotos(photo1, photo2)
     */
    @Insert
    fun insertPhotos(vararg photos: Photo): List<Long>

    // -- Read --

    /**
     * Usage:
     * dao.getPhotoByRealEstateId(realEstateId)
     *    .observe(this, Observer { photos -> ... })
     */
    @Query("SELECT * FROM photo WHERE real_estate_id = :realEstateId")
    fun getPhotoByRealEstateId(realEstateId: Long): LiveData<List<Photo>>

    /**
     * Usage:
     * dao.getAllPhotos()
     *    .observe(this, Observer { photos -> ... })
     */
    @Query("SELECT * FROM photo")
    fun getAllPhotos(): LiveData<List<Photo>>

    // -- Update --

    /**
     * Usage:
     * val numberOfUpdatedRow = dao.updatePhoto(photo)
     */
    @Update
    fun updatePhoto(photo: Photo): Int

    // -- Delete --

    /**
     * Usage:
     * val numberOfDeletedRow = dao.deletePhoto(photo)
     */
    @Delete
    fun deletePhoto(photo: Photo): Int
}