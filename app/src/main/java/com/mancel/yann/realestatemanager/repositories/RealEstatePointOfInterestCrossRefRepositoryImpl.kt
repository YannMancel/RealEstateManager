package com.mancel.yann.realestatemanager.repositories

import com.mancel.yann.realestatemanager.dao.RealEstatePointOfInterestCrossRefDAO
import com.mancel.yann.realestatemanager.models.RealEstatePointOfInterestCrossRef

/**
 * Created by Yann MANCEL on 05/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 *
 * A class which implements [RealEstatePointOfInterestCrossRefRepository].
 */
class RealEstatePointOfInterestCrossRefRepositoryImpl(
    private val mRealEstatePointOfInterestCrossRefDAO: RealEstatePointOfInterestCrossRefDAO
    ) : RealEstatePointOfInterestCrossRefRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    override fun insertCrossRef(crossRef: RealEstatePointOfInterestCrossRef): Long {
        TODO("Not yet implemented")
    }

    override fun insertSeveralCrossRef(vararg severalCrossRef: RealEstatePointOfInterestCrossRef): List<Long> {
        TODO("Not yet implemented")
    }
}