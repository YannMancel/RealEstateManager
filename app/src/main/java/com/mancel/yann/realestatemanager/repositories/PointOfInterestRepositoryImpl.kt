package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.dao.PointOfInterestDAO
import com.mancel.yann.realestatemanager.models.PointOfInterest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    override suspend fun insertPointOfInterest(
        pointOfInterest: PointOfInterest
    ): Long = withContext(Dispatchers.IO) {
        this@PointOfInterestRepositoryImpl.mPointOfInterestDAO.insertPointOfInterest(pointOfInterest)
    }

    // -- Read --

    override fun getAllPointsOfInterest(): LiveData<List<PointOfInterest>> =
        this.mPointOfInterestDAO.getAllPointsOfInterest()
}