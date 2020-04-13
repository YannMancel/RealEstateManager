package com.mancel.yann.realestatemanager.liveDatas

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.models.Photo

/**
 * Created by Yann MANCEL on 23/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.livedatas
 *
 * A [LiveData] of [List] of [Photo] subclass.
 */
class PhotoCreatorLiveData: LiveData<List<Photo>>() {

    // FIELDS --------------------------------------------------------------------------------------

    private val mPhotos: MutableList<Photo> = mutableListOf()

    // METHODS -------------------------------------------------------------------------------------

    // -- Photo --

    /**
     * Adds a [Photo]
     * @param photo a [Photo]
     */
    fun addPhoto(photo: Photo) {
        this.mPhotos.add(photo)

        // Notify
        this.value = this.mPhotos
    }

    /**
     * Updates a [Photo]
     * @param photo a [Photo]
     */
    fun updatePhoto(photo: Photo) {
        // The list does not contain more than one item with the same Url
        val index = this.mPhotos.indexOfFirst { it.mUrlPicture == photo.mUrlPicture }
        this.mPhotos[index] = photo

        // Notify
        this.value = this.mPhotos
    }

    /**
     * Deletes a [Photo]
     * @param photo a [Photo]
     */
    fun deletePhoto(photo: Photo) {
        this.mPhotos.remove(photo)

        // Notify
        this.value = this.mPhotos
    }
}