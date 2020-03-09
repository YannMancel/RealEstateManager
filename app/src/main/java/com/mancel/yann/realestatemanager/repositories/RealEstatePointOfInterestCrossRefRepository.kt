package com.mancel.yann.realestatemanager.repositories

import com.mancel.yann.realestatemanager.models.RealEstatePointOfInterestCrossRef

/**
 * Created by Yann MANCEL on 06/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 */
interface RealEstatePointOfInterestCrossRefRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    fun insertCrossRef(crossRef: RealEstatePointOfInterestCrossRef): Long

    fun insertSeveralCrossRef(vararg severalCrossRef: RealEstatePointOfInterestCrossRef): List<Long>
}