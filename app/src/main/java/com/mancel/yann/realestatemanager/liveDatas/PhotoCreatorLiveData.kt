package com.mancel.yann.realestatemanager.liveDatas

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.models.Photo
import timber.log.Timber

/**
 * Created by Yann MANCEL on 23/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.livedatas
 *
 * A [LiveData] of [List] of [Photo] subclass.
 */
class PhotoCreatorLiveData: LiveData<List<Photo>>() {

    // FIELDS --------------------------------------------------------------------------------------

    private val mPhotos = mutableListOf<Photo>()
    private val mAlreadyPresentPhotos = mutableListOf<Photo>()

    // METHODS -------------------------------------------------------------------------------------

    // -- Photo --

    /**
     * Adds all current [Photo]
     * @param currentPhotos a [List] of [Photo]
     */
    fun addCurrentPhotos(currentPhotos: List<Photo>) {
        // MODE EDIT
        with(this.mAlreadyPresentPhotos) {
            clear()
            addAll(currentPhotos)
        }

        // Add photos if possible
        this.mAlreadyPresentPhotos.forEach { photoFromDB ->
            // Search if already present
            val index = this.mPhotos.indexOfFirst {
                it.mUrlPicture == photoFromDB.mUrlPicture
            }

            if (index == -1) {
                // New photo
                this.mPhotos.add(photoFromDB)
            }
            else {
                // Update photo
                this.mPhotos[index] = photoFromDB
            }
        }

        // Notify
        this.value = this.mPhotos
    }

    /**
     * Adds a [Photo]
     * @param photo a [Photo]
     */
    fun addPhoto(photo: Photo) {
        // Search presence in photos from database
        val newPhoto = this.mAlreadyPresentPhotos.find {
            it.mUrlPicture == photo.mUrlPicture
        } ?: photo

        // Particular case: Photo from database -> removed then added again
        if (newPhoto.mId != 0L) {
            newPhoto.mDescription = photo.mDescription
        }

        this.mPhotos.add(newPhoto)

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