package com.mancel.yann.realestatemanager.dao

import androidx.room.Dao
import androidx.room.Insert
import com.mancel.yann.realestatemanager.models.RealEstatePointOfInterestCrossRef

/**
 * Created by Yann MANCEL on 26/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.dao
 *
 * DAO of [RealEstatePointOfInterestCrossRef].
 */
@Dao
interface RealEstatePointOfInterestCrossRefDAO {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    /**
     * Usage:
     * val id = dao.insertCrossRef(crossRef)
     */
    @Insert
    fun insertCrossRef(crossRef: RealEstatePointOfInterestCrossRef): Long

    /**
     * Usage:
     * val id = dao.insertSeveralCrossRef(crossRef1, crossRef2)
     */
    @Insert
    fun insertSeveralCrossRef(vararg severalCrossRef: RealEstatePointOfInterestCrossRef): List<Long>
}