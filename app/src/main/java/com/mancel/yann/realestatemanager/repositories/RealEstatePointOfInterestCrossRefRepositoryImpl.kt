package com.mancel.yann.realestatemanager.repositories

import com.mancel.yann.realestatemanager.dao.RealEstatePointOfInterestCrossRefDAO
import com.mancel.yann.realestatemanager.models.RealEstatePointOfInterestCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Yann MANCEL on 05/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 *
 * A class which implements [RealEstatePointOfInterestCrossRefRepository].
 */
class RealEstatePointOfInterestCrossRefRepositoryImpl(
    private val mCrossRefDAO: RealEstatePointOfInterestCrossRefDAO
) : RealEstatePointOfInterestCrossRefRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    override suspend fun insertCrossRef(
        crossRef: RealEstatePointOfInterestCrossRef
    ): Long = withContext(Dispatchers.IO) {
        this@RealEstatePointOfInterestCrossRefRepositoryImpl.mCrossRefDAO.insertCrossRef(crossRef)
    }
}