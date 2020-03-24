package com.mancel.yann.realestatemanager.views.dialogs

import com.mancel.yann.realestatemanager.models.Photo

/**
 * Created by Yann MANCEL on 21/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.dialogs
 */
interface DialogListener {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Called when the user has selected a [Photo]
     * @param photo the selected [Photo]
     * @param mode  a [PhotoDialogFragment.PhotoDialogMode]
     */
    fun getSelectedPhotoFromDialog(photo: Photo, mode: PhotoDialogFragment.PhotoDialogMode)
}