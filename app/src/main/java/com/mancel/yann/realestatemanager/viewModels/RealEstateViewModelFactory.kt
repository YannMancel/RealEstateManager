package com.mancel.yann.realestatemanager.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mancel.yann.realestatemanager.repositories.*

/**
 * Created by Yann MANCEL on 09/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.viewModels
 *
 * A class which implements [ViewModelProvider.Factory].
 */
class RealEstateViewModelFactory(
    private val mPlaceRepository: PlaceRepository,
    private val mUserRepository: UserRepository,
    private val mRealEstateRepository: RealEstateRepository,
    private val mPhotoRepository: PhotoRepository,
    private val mPointOfInterestRepository: PointOfInterestRepository,
    private val mRealEstatePointOfInterestCrossRefRepository: RealEstatePointOfInterestCrossRefRepository
) : ViewModelProvider.Factory {

    // METHODS -------------------------------------------------------------------------------------

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RealEstateViewModel::class.java)) {
            return RealEstateViewModel(
                this.mPlaceRepository,
                this.mUserRepository,
                this.mRealEstateRepository,
                this.mPhotoRepository,
                this.mPointOfInterestRepository,
                this.mRealEstatePointOfInterestCrossRefRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}