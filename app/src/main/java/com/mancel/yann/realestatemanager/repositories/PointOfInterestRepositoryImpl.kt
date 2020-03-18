package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.dao.PointOfInterestDAO
import com.mancel.yann.realestatemanager.models.PointOfInterest

/**
 * Created by Yann MANCEL on 05/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 *
 * A class which implements [PointOfInterestRepository].
 */
class PointOfInterestRepositoryImpl(
    private val mPointOfInterestDAO: PointOfInterestDAO
    ) : PointOfInterestRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    override suspend fun insertPointOfInterest(pointOfInterest: PointOfInterest): Long =
        this.mPointOfInterestDAO.insertPointOfInterest(pointOfInterest)

    override suspend fun insertPointsOfInterest(vararg pointsOfInterest: PointOfInterest): List<Long> =
        this.mPointOfInterestDAO.insertPointsOfInterest(*pointsOfInterest)

    // -- Read --

    override fun getAllPointsOfInterest(): LiveData<List<PointOfInterest>> =
        this.mPointOfInterestDAO.getAllPointsOfInterest()
}