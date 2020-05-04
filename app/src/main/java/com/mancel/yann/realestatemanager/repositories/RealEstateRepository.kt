package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.models.RealEstate
import com.mancel.yann.realestatemanager.models.RealEstateWithPhotos
import com.mancel.yann.realestatemanager.models.RealEstateWithPointsOfInterest

/**
 * Created by Yann MANCEL on 06/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 */
interface RealEstateRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    suspend fun insertRealEstate(realEstate: RealEstate): Long

    // -- Read --

    fun getRealEstatesWithPhotosByUserId(userId: Long): LiveData<List<RealEstateWithPhotos>>

    fun getRealEstateWithPhotosById(realEstateId: Long): LiveData<RealEstateWithPhotos>

    fun getRealEstateWithPointsOfInterestById(
        realEstateId: Long
    ): LiveData<RealEstateWithPointsOfInterest>

    fun getRealEstatesWithPhotosByMultiSearch(
        minPrice: Double = 0.0,
        maxPrice: Double = Double.MAX_VALUE,
        minSurface: Double = 0.0,
        maxSurface: Double = Double.MAX_VALUE,
        minNumberRoom: Int = 0,
        maxNumberRoom: Int = Int.MAX_VALUE
    ): LiveData<List<RealEstateWithPhotos>>

    // -- Update --

    suspend fun updateRealEstate(realEstate: RealEstate): Int
}