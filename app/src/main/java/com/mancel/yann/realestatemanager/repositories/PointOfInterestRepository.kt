package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.models.PointOfInterest

/**
 * Created by Yann MANCEL on 06/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 */
interface PointOfInterestRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    suspend fun insertPointOfInterest(pointOfInterest: PointOfInterest): Long

    // -- Read --

    fun getAllPointsOfInterest(): LiveData<List<PointOfInterest>>
}